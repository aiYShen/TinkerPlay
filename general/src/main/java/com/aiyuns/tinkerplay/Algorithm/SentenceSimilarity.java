package com.aiyuns.tinkerplay.Algorithm;

import com.aiyuns.tinkerplay.Other.Demo.GloVeModelLoaderDemo;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import java.util.List;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * @Author: aiYunS @Date: 2024年2月29日, 0029 上午 8:44:44 @Description: 汉语语句相似度算法
 */
public class SentenceSimilarity {
  private static final GloVeModelLoaderDemo loader;

  static {
    loader = new GloVeModelLoaderDemo("E:\\aiyuns\\sgns.wiki.bigram-char.txt");
  }

  public static void main(String[] args) {
    String sentence1 = "这里是中国";
    String sentence2 = "中国万岁中国万岁中国万岁这里是中国中国万岁中国万岁中国万岁中国万岁中国万岁";

    List<Term> terms1 = HanLP.segment(sentence1);
    List<Term> terms2 = HanLP.segment(sentence2);

    RealVector vector1 = getAverageWordVector(terms1);
    RealVector vector2 = getAverageWordVector(terms2);

    double similarity = calculateCosineSimilarity(vector1, vector2);
    System.out.println("句子相似度：" + similarity);
  }

  private static RealVector getAverageWordVector(List<Term> terms) {
    // 假设词向量维度为100
    int dimensions = 300;
    RealVector sumVector = new ArrayRealVector(dimensions);
    int count = 0;
    for (Term term : terms) {
      RealVector wordVector = getWordVector(term.word);
      if (wordVector != null) {
        sumVector = sumVector.add(wordVector);
        count++;
      }
    }
    return sumVector.mapDivide(count);
  }

  private static RealVector getWordVector(String word) {
    // 根据具体词向量模型获取词向量
    // 这里假设使用的是预训练的词向量模型
    // 实际情况需要根据所选的词向量模型进行实现
    // 这里仅作示例，实际中需要根据实际情况替换为相应的代码
    RealVector vector = loader.getWordVector(word);
    return vector;
  }

  private static double calculateCosineSimilarity(RealVector vector1, RealVector vector2) {
    return (vector1.dotProduct(vector2)) / (vector1.getNorm() * vector2.getNorm());
  }
}
