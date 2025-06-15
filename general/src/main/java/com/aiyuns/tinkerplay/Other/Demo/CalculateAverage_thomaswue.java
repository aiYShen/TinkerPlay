/*
 *  版权所有 2023 原始作者
 *
 *  根据 Apache 许可证 2.0 版（“许可证”）授权；
 *  除非符合许可证要求，否则不得使用此文件。
 *  您可以在以下地址获取许可证副本：
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  除非适用法律要求或书面同意，根据许可证分发的软件
 *  按“原样”分发，不提供任何明示或暗示的担保或条件。
 *  请参阅许可证以获取有关权限和限制的具体语言。
 */
package com.aiyuns.tinkerplay.Other.Demo;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 该解决方案启动一个子工作进程来执行实际工作，以便在主进程已经返回结果时，可以清理内存映射。
 * 工作进程会对输入文件进行内存映射，为每个可用核心创建一个工作线程，然后一次处理大小为 {@link #SEGMENT_SIZE} 的段。
 * 这些段被分成三个部分，每个部分的游标在同一线程中同时处理该段。结果被累积到 {@link Result} 对象中，
 * 并使用树映射在最后按顺序累积结果。在 Intel i9-13900K 上运行时间为 0.31 秒，而参考实现需要 120.37 秒。
 * 致谢：Quan Anh Mai 提供了无分支数字解析代码；Alfonso² Peterssen 提出了使用 unsafe 进行内存映射和子进程的建议；
 * Artsiom Korzun 展示了在 2MB 段上使用工作窃取的好处，而不是在工作线程间平均分配；
 * Jaromir Hamala 展示了避免 <8 和 8-16 情况之间的分支预测失误是一个重大优化，即使需要执行更多工作；
 * Van Phu DO 展示了基于掩码的查找表优于位移操作。
 */
public class CalculateAverage_thomaswue {
  private static final String FILE = "./measurements.txt";
  private static final int MIN_TEMP = -999;
  private static final int MAX_TEMP = 999;
  private static final int MAX_NAME_LENGTH = 100;
  private static final int MAX_CITIES = 10000;
  private static final int SEGMENT_SIZE = 1 << 21;
  private static final int HASH_TABLE_SIZE = 1 << 17;

  public static void main(String[] args) throws IOException, InterruptedException {
    // 如果当前进程不是工作进程，则启动工作子进程。
    if (args.length == 0 || !("--worker".equals(args[0]))) {
      spawnWorker();
      return;
    }

    int numberOfWorkers = Runtime.getRuntime().availableProcessors();
    try (var fileChannel =
        FileChannel.open(java.nio.file.Path.of(FILE), java.nio.file.StandardOpenOption.READ)) {
      long fileSize = fileChannel.size();
      final long fileStart =
          fileChannel
              .map(FileChannel.MapMode.READ_ONLY, 0, fileSize, java.lang.foreign.Arena.global())
              .address();
      final long fileEnd = fileStart + fileSize;
      final AtomicLong cursor = new AtomicLong(fileStart);

      // 并行处理段
      Thread[] threads = new Thread[numberOfWorkers];
      List<Result>[] allResults = new List[numberOfWorkers];
      for (int i = 0; i < threads.length; ++i) {
        final int index = i;
        threads[i] =
            new Thread(
                () -> {
                  List<Result> results = new ArrayList<>(MAX_CITIES);
                  parseLoop(cursor, fileEnd, fileStart, results);
                  allResults[index] = results;
                });
        threads[i].start();
      }
      for (Thread thread : threads) {
        thread.join();
      }

      // 最终输出
      System.out.println(accumulateResults(allResults));
      System.out.close();
    }
  }

  private static void spawnWorker() throws IOException {
    ProcessHandle.Info info = ProcessHandle.current().info();
    ArrayList<String> workerCommand = new ArrayList<>();
    info.command().ifPresent(workerCommand::add);
    info.arguments().ifPresent(args -> workerCommand.addAll(Arrays.asList(args)));
    workerCommand.add("--worker");
    new ProcessBuilder()
        .command(workerCommand)
        .inheritIO()
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()
        .getInputStream()
        .transferTo(System.out);
  }

  private static TreeMap<String, Result> accumulateResults(List<Result>[] allResults) {
    TreeMap<String, Result> result = new TreeMap<>();
    for (List<Result> resultArr : allResults) {
      for (Result r : resultArr) {
        Result current = result.putIfAbsent(r.calcName(), r);
        if (current != null) {
          current.accumulate(r);
        }
      }
    }
    return result;
  }

  private static void parseLoop(
      AtomicLong counter, long fileEnd, long fileStart, List<Result> collectedResults) {
    Result[] results = new Result[HASH_TABLE_SIZE];
    while (true) {
      long current = counter.addAndGet(SEGMENT_SIZE) - SEGMENT_SIZE;
      if (current >= fileEnd) {
        return;
      }

      long segmentEnd = nextNewLine(Math.min(fileEnd - 1, current + SEGMENT_SIZE));
      long segmentStart;
      if (current == fileStart) {
        segmentStart = current;
      } else {
        segmentStart = nextNewLine(current) + 1;
      }

      long dist = (segmentEnd - segmentStart) / 3;
      long midPoint1 = nextNewLine(segmentStart + dist);
      long midPoint2 = nextNewLine(segmentStart + dist + dist);

      Scanner scanner1 = new Scanner(segmentStart, midPoint1);
      Scanner scanner2 = new Scanner(midPoint1 + 1, midPoint2);
      Scanner scanner3 = new Scanner(midPoint2 + 1, segmentEnd);
      while (true) {
        if (!scanner1.hasNext()) {
          break;
        }
        if (!scanner2.hasNext()) {
          break;
        }
        if (!scanner3.hasNext()) {
          break;
        }
        long word1 = scanner1.getLong();
        long word2 = scanner2.getLong();
        long word3 = scanner3.getLong();
        long delimiterMask1 = findDelimiter(word1);
        long delimiterMask2 = findDelimiter(word2);
        long delimiterMask3 = findDelimiter(word3);
        long word1b = scanner1.getLongAt(scanner1.pos() + 8);
        long word2b = scanner2.getLongAt(scanner2.pos() + 8);
        long word3b = scanner3.getLongAt(scanner3.pos() + 8);
        long delimiterMask1b = findDelimiter(word1b);
        long delimiterMask2b = findDelimiter(word2b);
        long delimiterMask3b = findDelimiter(word3b);
        Result existingResult1 =
            findResult(
                word1,
                delimiterMask1,
                word1b,
                delimiterMask1b,
                scanner1,
                results,
                collectedResults);
        Result existingResult2 =
            findResult(
                word2,
                delimiterMask2,
                word2b,
                delimiterMask2b,
                scanner2,
                results,
                collectedResults);
        Result existingResult3 =
            findResult(
                word3,
                delimiterMask3,
                word3b,
                delimiterMask3b,
                scanner3,
                results,
                collectedResults);
        long number1 = scanNumber(scanner1);
        long number2 = scanNumber(scanner2);
        long number3 = scanNumber(scanner3);
        record(existingResult1, number1);
        record(existingResult2, number2);
        record(existingResult3, number3);
      }

      while (scanner1.hasNext()) {
        long word = scanner1.getLong();
        long pos = findDelimiter(word);
        long wordB = scanner1.getLongAt(scanner1.pos() + 8);
        long posB = findDelimiter(wordB);
        record(
            findResult(word, pos, wordB, posB, scanner1, results, collectedResults),
            scanNumber(scanner1));
      }
      while (scanner2.hasNext()) {
        long word = scanner2.getLong();
        long pos = findDelimiter(word);
        long wordB = scanner2.getLongAt(scanner2.pos() + 8);
        long posB = findDelimiter(wordB);
        record(
            findResult(word, pos, wordB, posB, scanner2, results, collectedResults),
            scanNumber(scanner2));
      }
      while (scanner3.hasNext()) {
        long word = scanner3.getLong();
        long pos = findDelimiter(word);
        long wordB = scanner3.getLongAt(scanner3.pos() + 8);
        long posB = findDelimiter(wordB);
        record(
            findResult(word, pos, wordB, posB, scanner3, results, collectedResults),
            scanNumber(scanner3));
      }
    }
  }

  private static final long[] MASK1 =
      new long[] {
        0xFFL,
        0xFFFFL,
        0xFFFFFFL,
        0xFFFFFFFFL,
        0xFFFFFFFFFFL,
        0xFFFFFFFFFFFFL,
        0xFFFFFFFFFFFFFFL,
        0xFFFFFFFFFFFFFFFFL,
        0xFFFFFFFFFFFFFFFFL
      };
  private static final long[] MASK2 =
      new long[] {0x00L, 0x00L, 0x00L, 0x00L, 0x00L, 0x00L, 0x00L, 0x00L, 0xFFFFFFFFFFFFFFFFL};

  private static Result findResult(
      long initialWord,
      long initialDelimiterMask,
      long wordB,
      long delimiterMaskB,
      Scanner scanner,
      Result[] results,
      List<Result> collectedResults) {
    Result existingResult;
    long word = initialWord;
    long delimiterMask = initialDelimiterMask;
    long hash;
    long nameAddress = scanner.pos();
    long word2 = wordB;
    long delimiterMask2 = delimiterMaskB;
    if ((delimiterMask | delimiterMask2) != 0) {
      int letterCount1 = Long.numberOfTrailingZeros(delimiterMask) >>> 3; // value值 在 1 - 8 之间
      int letterCount2 = Long.numberOfTrailingZeros(delimiterMask2) >>> 3; // value值 在 0 - 8 之间
      long mask = MASK2[letterCount1];
      word = word & MASK1[letterCount1];
      word2 = mask & word2 & MASK1[letterCount2];
      hash = word ^ word2;
      existingResult = results[hashToIndex(hash, results)];
      scanner.add(letterCount1 + (letterCount2 & mask));
      if (existingResult != null
          && existingResult.firstNameWord == word
          && existingResult.secondNameWord == word2) {
        return existingResult;
      }
    } else {
      // 当在前 16 个字节中找不到“;”时，采用慢路径
      hash = word ^ word2;
      scanner.add(16);
      while (true) {
        word = scanner.getLong();
        delimiterMask = findDelimiter(word);
        if (delimiterMask != 0) {
          int trailingZeros = Long.numberOfTrailingZeros(delimiterMask);
          word = (word << (63 - trailingZeros));
          scanner.add(trailingZeros >>> 3);
          hash ^= word;
          break;
        } else {
          scanner.add(8);
          hash ^= word;
        }
      }
    }

    // 将名称的长度保存以供后续使用
    int nameLength = (int) (scanner.pos() - nameAddress);

    // 计算哈希表的最终索引
    int tableIndex = hashToIndex(hash, results);
    outer:
    while (true) {
      existingResult = results[tableIndex];
      if (existingResult == null) {
        existingResult =
            newEntry(results, nameAddress, tableIndex, nameLength, scanner, collectedResults);
      }
      // 检查冲突
      int i = 0;
      for (; i < nameLength + 1 - 8; i += 8) {
        if (scanner.getLongAt(existingResult.nameAddress + i)
            != scanner.getLongAt(nameAddress + i)) {
          // 发生冲突错误，尝试下一个
          tableIndex = (tableIndex + 31) & (results.length - 1);
          continue outer;
        }
      }

      int remainingShift = (64 - ((nameLength + 1 - i) << 3));
      if (((scanner.getLongAt(existingResult.nameAddress + i)
                  ^ (scanner.getLongAt(nameAddress + i)))
              << remainingShift)
          == 0) {
        break;
      } else {
        // 发生冲突错误，尝试下一个
        tableIndex = (tableIndex + 31) & (results.length - 1);
      }
    }
    return existingResult;
  }

  private static long nextNewLine(long prev) {
    while (true) {
      long currentWord = Scanner.UNSAFE.getLong(prev);
      long input = currentWord ^ 0x0A0A0A0A0A0A0A0AL;
      long pos = (input - 0x0101010101010101L) & ~input & 0x8080808080808080L;
      if (pos != 0) {
        prev += Long.numberOfTrailingZeros(pos) >>> 3;
        break;
      } else {
        prev += 8;
      }
    }
    return prev;
  }

  private static long scanNumber(Scanner scanPtr) {
    long numberWord = scanPtr.getLongAt(scanPtr.pos() + 1);
    int decimalSepPos = Long.numberOfTrailingZeros(~numberWord & 0x10101000L);
    long number = convertIntoNumber(decimalSepPos, numberWord);
    scanPtr.add((decimalSepPos >>> 3) + 4);
    return number;
  }

  private static void record(Result existingResult, long number) {
    if (number < existingResult.min) {
      existingResult.min = (short) number;
    }
    if (number > existingResult.max) {
      existingResult.max = (short) number;
    }
    existingResult.sum += number;
    existingResult.count++;
  }

  private static int hashToIndex(long hash, Result[] results) {
    long hashAsInt = hash ^ (hash >>> 33) ^ (hash >>> 15);
    return (int) (hashAsInt & (results.length - 1));
  }

  // 由 Quan Anh Mai 创建的特殊方法，用于将 ASCII 数字转换为整数，且不使用分支
  private static long convertIntoNumber(int decimalSepPos, long numberWord) {
    int shift = 28 - decimalSepPos;
    // 如果是负数，signed 为 -1，否则为 0
    long signed = (~numberWord << 59) >> 63;
    long designMask = ~(signed & 0xFF);
    // 将数字对齐到特定位置，并将 ASCII 转换为数字值
    long digits = ((numberWord & designMask) << shift) & 0x0F000F0F00L;
    // 现在 digits 的形式为 0xUU00TTHH00（UU：个位，TT：十位，HH：百位）
    // 0xUU00TTHH00 * (100 * 0x1000000 + 10 * 0x10000 + 1) =
    // 0x000000UU00TTHH00 + 0x00UU00TTHH000000 * 10 + 0xUU00TTHH00000000 * 100
    long absValue = ((digits * 0x640a0001) >>> 32) & 0x3FF;
    return (absValue ^ signed) - signed;
  }

  private static long findDelimiter(long word) {
    long input = word ^ 0x3B3B3B3B3B3B3B3BL;
    return (input - 0x0101010101010101L) & ~input & 0x8080808080808080L;
  }

  private static Result newEntry(
      Result[] results,
      long nameAddress,
      int hash,
      int nameLength,
      Scanner scanner,
      List<Result> collectedResults) {
    Result r = new Result();
    results[hash] = r;
    int totalLength = nameLength + 1;
    r.firstNameWord = scanner.getLongAt(nameAddress);
    r.secondNameWord = scanner.getLongAt(nameAddress + 8);
    if (totalLength <= 8) {
      r.firstNameWord = r.firstNameWord & MASK1[totalLength - 1];
      r.secondNameWord = 0;
    } else if (totalLength < 16) {
      r.secondNameWord = r.secondNameWord & MASK1[totalLength - 9];
    }
    r.nameAddress = nameAddress;
    collectedResults.add(r);
    return r;
  }

  private static final class Result {
    long firstNameWord, secondNameWord;
    short min, max;
    int count;
    long sum;
    long nameAddress;

    private Result() {
      this.min = MAX_TEMP;
      this.max = MIN_TEMP;
    }

    public String toString() {
      return round(((double) min) / 10.0)
          + "/"
          + round((((double) sum) / 10.0) / count)
          + "/"
          + round(((double) max) / 10.0);
    }

    private static double round(double value) {
      return Math.round(value * 10.0) / 10.0;
    }

    private void accumulate(Result other) {
      if (other.min < min) {
        min = other.min;
      }
      if (other.max > max) {
        max = other.max;
      }
      sum += other.sum;
      count += other.count;
    }

    public String calcName() {
      Scanner scanner = new Scanner(nameAddress, nameAddress + MAX_NAME_LENGTH + 1);
      int nameLength = 0;
      while (scanner.getByteAt(nameAddress + nameLength) != ';') {
        nameLength++;
      }
      byte[] array = new byte[nameLength];
      for (int i = 0; i < nameLength; ++i) {
        array[i] = scanner.getByteAt(nameAddress + i);
      }
      return new String(array, java.nio.charset.StandardCharsets.UTF_8);
    }
  }

  private static final class Scanner {
    private static final sun.misc.Unsafe UNSAFE = initUnsafe();
    private long pos;
    private final long end;

    private static sun.misc.Unsafe initUnsafe() {
      try {
        java.lang.reflect.Field theUnsafe = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (sun.misc.Unsafe) theUnsafe.get(sun.misc.Unsafe.class);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    public Scanner(long start, long end) {
      this.pos = start;
      this.end = end;
    }

    boolean hasNext() {
      return pos < end;
    }

    long pos() {
      return pos;
    }

    void add(long delta) {
      pos += delta;
    }

    long getLong() {
      return UNSAFE.getLong(pos);
    }

    long getLongAt(long pos) {
      return UNSAFE.getLong(pos);
    }

    byte getByteAt(long pos) {
      return UNSAFE.getByte(pos);
    }
  }
}
