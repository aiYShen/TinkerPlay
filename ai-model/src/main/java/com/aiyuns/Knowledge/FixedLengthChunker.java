package com.aiyuns.Knowledge;

import java.util.ArrayList;
import java.util.List;

/**
 * 固定长度切片（Fixed-Length Chunking）. 根据固定字符数或 token 数切片。最简单的方法，不考虑语义边界。优点：实现容易、速度快。缺点：可能切断句子，导致语义不完整
 */
public class FixedLengthChunker {
  public static List<String> chunk(String text, int chunkSize) {
    List<String> chunks = new ArrayList<>();
    for (int i = 0; i < text.length(); i += chunkSize) {
      int end = Math.min(i + chunkSize, text.length());
      chunks.add(text.substring(i, end));
    }
    return chunks;
  }

  public static void main(String[] args) {
    String knowledge = "这是一个关于AI大模型的知识库示例。知识调用需要切片以适应上下文窗口。RAG系统常用此方法。";
    List<String> result = chunk(knowledge, 20); // 每个切片20字符
    result.forEach(System.out::println);
    // 输出示例：
    // 这是一个关于AI大模型的
    // 知识库示例。知识调用需要
    // 切片以适应上下文窗口。R
    // AG系统常用此方法。
  }
}
