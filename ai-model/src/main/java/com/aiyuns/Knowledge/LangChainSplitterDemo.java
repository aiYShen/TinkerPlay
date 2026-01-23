package com.aiyuns.Knowledge;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import java.util.List;

/** 递归字符切片 (Recursive Character Splitting). */
public class LangChainSplitterDemo {
  public static void main(String[] args) {
    // 1. 模拟长文本
    String longText =
        "这是第一段，讨论Java多线程。\n\n"
            + "这是第二段，非常长。Java 工程师需要掌握并发包(JUC)。"
            + "线程池的原理是基于 ThreadPoolExecutor 类的实现。"
            + "核心参数包括 corePoolSize, maximumPoolSize 等。";

    Document document = Document.from(longText, Metadata.from("source", "manual.pdf"));

    // 2. 创建递归切片器
    // maxSegmentSizeInChars: 50 (每块最多50字)
    // maxOverlapSizeInChars: 10 (重叠10字保持连贯)
    DocumentSplitter splitter = DocumentSplitters.recursive(50, 10);

    // 3. 执行切片
    List<TextSegment> segments = splitter.split(document);

    // 4. 打印结果
    for (int i = 0; i < segments.size(); i++) {
      System.out.printf(
          "Chunk %d [%d chars]: %s%n", i, segments.get(i).text().length(), segments.get(i).text());
    }
  }
}
