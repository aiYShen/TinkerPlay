package com.aiyuns.tinkerplay.Other.Demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * @Author: aiYunS @Date: 2024年2月29日, 0029 上午 9:42:19 @Description: 加载词向量模型
 */
public class GloVeModelLoaderDemo {

  private Map<String, RealVector> wordVectors;

  public GloVeModelLoaderDemo(String filePath) {
    wordVectors = loadModel(filePath);
  }

  private Map<String, RealVector> loadModel(String filePath) {
    Map<String, RealVector> model = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(" ");
        String word = parts[0];
        double[] vectorValues = new double[parts.length - 1];
        for (int i = 1; i < parts.length; i++) {
          vectorValues[i - 1] = Double.parseDouble(parts[i]);
        }
        RealVector vector = new ArrayRealVector(vectorValues);
        model.put(word, vector);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return model;
  }

  public RealVector getWordVector(String word) {
    return wordVectors.get(word);
  }

  public static void main(String[] args) {
    String filePath = "/Users/yuxinbai/aiyuns/glove.6B.50d.txt";
    GloVeModelLoaderDemo loader = new GloVeModelLoaderDemo(filePath);

    // Example usage:
    String word = "example";
    RealVector vector = loader.getWordVector(word);
    System.out.println("Word vector for \"" + word + "\": " + vector);
  }
}
