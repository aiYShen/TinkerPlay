package com.aiyuns.Knowledge;

import java.util.ArrayList;
import java.util.List;

/** 基于段落切片（Paragraph-Based Chunking）. * 以段落为单位切片，使用换行符或空行作为边界。优点：适合结构化文档，保留更大语义块。缺点：段落过长需结合其他方法 */
public class ParagraphChunker {
  public static List<String> chunk(String text) {
    List<String> chunks = new ArrayList<>();
    String[] paragraphs = text.split("\n\n"); // 假设段落由两个换行分隔
    for (String para : paragraphs) {
      String trimmed = para.trim();
      if (!trimmed.isEmpty()) {
        chunks.add(trimmed);
      }
    }
    return chunks;
  }

  public static void main(String[] args) {
    String knowledge = "段落1: 这是一个关于AI大模型的知识库示例。\n\n段落2: 知识调用需要切片以适应上下文窗口。\n\n段落3: RAG系统常用此方法。";
    List<String> result = chunk(knowledge);
    result.forEach(System.out::println);
    // 输出示例：
    // 段落1: 这是一个关于AI大模型的知识库示例。
    // 段落2: 知识调用需要切片以适应上下文窗口。
    // 段落3: RAG系统常用此方法。
  }
}
