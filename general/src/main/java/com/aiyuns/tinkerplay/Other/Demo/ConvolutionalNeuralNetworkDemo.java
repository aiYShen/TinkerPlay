package com.aiyuns.tinkerplay.Other.Demo;

import java.util.Arrays;

/**
 * @Author: aiYunS @Date: 2024年3月8日, 0008 下午 2:56:27 @Description: 卷积神经网络 Gemini
 */
public class ConvolutionalNeuralNetworkDemo {

  public static void main(String[] args) {

    // 定义输入数据
    int[][] input =
        new int[][] {
          {1, 2, 3},
          {4, 5, 6},
          {7, 8, 9}
        };

    // 定义卷积核
    int[][] kernel =
        new int[][] {
          {1, 0, -1},
          {0, 1, 0},
          {-1, 0, 1}
        };

    // 进行卷积操作
    int[][] output = convolve(input, kernel);

    // 打印输出结果
    for (int[] row : output) {
      System.out.println(Arrays.toString(row));
    }
  }

  private static int[][] convolve(int[][] input, int[][] kernel) {

    int outputWidth = input.length - kernel.length + 1;
    int outputHeight = input[0].length - kernel[0].length + 1;

    int[][] output = new int[outputWidth][outputHeight];

    for (int i = 0; i < outputWidth; i++) {
      for (int j = 0; j < outputHeight; j++) {
        for (int k = 0; k < kernel.length; k++) {
          for (int l = 0; l < kernel[0].length; l++) {
            output[i][j] += input[i + k][j + l] * kernel[k][l];
          }
        }
      }
    }

    return output;
  }
}
