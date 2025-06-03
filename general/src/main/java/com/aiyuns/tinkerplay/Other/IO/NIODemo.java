package com.aiyuns.tinkerplay.Other.IO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author: aiYunS @Date: 2023年5月18日, 0018 上午 9:36:20 @Description: NIO小例子
 */
public class NIODemo {

  public static void main(String[] args) {
    NIOread("src/main/resources/db.properties");
  }

  public static void NIOread(String path) {
    FileChannel fileChannel = null;
    try {
      // 打开文件通道
      fileChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);

      // 创建缓冲区
      ByteBuffer buffer = ByteBuffer.allocate(1024);

      // 从文件通道读取数据到缓冲区
      int bytesRead = fileChannel.read(buffer);
      while (bytesRead != -1) {
        // 切换为读模式
        buffer.flip();

        // 从缓冲区读取数据
        while (buffer.hasRemaining()) {
          byte data = buffer.get();
          System.out.print((char) data);
        }

        // 清空缓冲区，准备下一次读取
        buffer.clear();

        // 继续从文件通道读取数据到缓冲区
        bytesRead = fileChannel.read(buffer);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // 关闭文件通道
      if (fileChannel != null) {
        try {
          fileChannel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
