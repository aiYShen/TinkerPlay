package com.aiyuns.Knowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 基于句子切片（Sentence-Based Chunking）. 以句子为单位切片，通常使用标点（如句号、问号）作为边界。优点：保持基本语义完整。缺点：长句可能仍超出限制，需要进一步拆分 */
public class SentenceChunker {
  public static List<String> chunk(String text) {
    List<String> chunks = new ArrayList<>();
    Pattern sentencePattern = Pattern.compile("([^。！？]*[。！？])");
    Matcher matcher = sentencePattern.matcher(text);
    int end = 0;
    while (matcher.find()) {
      String sentence = matcher.group().trim();
      if (!sentence.isEmpty()) {
        chunks.add(sentence);
      }
      end = matcher.end();
    }
    // 处理剩余部分（无标点结尾）
    if (end < text.length()) {
      chunks.add(text.substring(matcher.end()).trim());
    }
    return chunks;
  }

  public static void main(String[] args) {
    String knowledge = "这是一个关于AI大模型的知识库示例。知识调用需要切片以适应上下文窗口。RAG系统常用此方法。还有更多方法如语义切片。";
    List<String> result = chunk(knowledge);
    result.forEach(System.out::println);
    // 输出示例：
    // 这是一个关于AI大模型的知识库示例。
    // 知识调用需要切片以适应上下文窗口。
    // RAG系统常用此方法。
    // 还有更多方法如语义切片。
  }
}
