package com.aiyuns.Knowledge;

import java.util.ArrayList;
import java.util.List;

/**
 * 语义切片. 使用 NLP 模型计算语义相似度，将相似内容聚合成切片。优点：高准确性，切片更具连贯性。缺点：计算密集，需要外部库（如 Sentence Transformers 的 Java
 * 版本或自定义实现）。这里简化使用余弦相似度示例（实际需嵌入向量）
 */
public class SemanticChunker {
  // 模拟嵌入向量（实际使用 NLP 模型生成）
  private static double cosineSimilarity(double[] vec1, double[] vec2) {
    double dotProduct = 0.0;
    double norm1 = 0.0, norm2 = 0.0;
    for (int i = 0; i < vec1.length; i++) {
      dotProduct += vec1[i] * vec2[i];
      norm1 += Math.pow(vec1[i], 2);
      norm2 += Math.pow(vec2[i], 2);
    }
    return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
  }

  public static List<String> chunk(List<String> sentences, double threshold) {
    List<String> chunks = new ArrayList<>();
    StringBuilder currentChunk = new StringBuilder();
    double[] prevEmbedding = getEmbedding(sentences.get(0)); // 模拟获取嵌入
    currentChunk.append(sentences.get(0));

    for (int i = 1; i < sentences.size(); i++) {
      double[] currEmbedding = getEmbedding(sentences.get(i));
      if (cosineSimilarity(prevEmbedding, currEmbedding) >= threshold) {
        currentChunk.append(" ").append(sentences.get(i));
      } else {
        chunks.add(currentChunk.toString());
        currentChunk = new StringBuilder(sentences.get(i));
      }
      prevEmbedding = currEmbedding;
    }
    if (currentChunk.length() > 0) {
      chunks.add(currentChunk.toString());
    }
    return chunks;
  }

  // 模拟嵌入生成（实际替换为 NLP 模型）
  private static double[] getEmbedding(String sentence) {
    return new double[] {Math.random(), Math.random(), Math.random()}; // 随机向量示例
  }

  public static void main(String[] args) {
    List<String> sentences = List.of("AI大模型知识调用。", "需要切片适应窗口。", "RAG系统常用。", "语义方法更先进。");
    List<String> result = chunk(sentences, 0.5); // 相似度阈值0.5
    result.forEach(System.out::println);
    // 输出示例（取决于随机向量）：
    // AI大模型知识调用。 需要切片适应窗口。
    // RAG系统常用。 语义方法更先进。
  }
}
