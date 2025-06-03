package com.aiyuns.tinkerplay.Other.PictureAndAudioAndVideo;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

// 特征提取和匹配: 边缘检测
public class ImageBoundaryAnalysis {

  public static void main(String[] args) {
    // 初始化 OpenCV
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    // 读取图片 替换为你的图片路径
    String imagePath = "/Users/yuxinbai/Desktop/face1.jpg";
    Mat image = Imgcodecs.imread(imagePath);

    if (image.empty()) {
      System.out.println("无法读取图片: " + imagePath);
      return;
    }

    // 将图片转换为灰度图
    Mat grayImage = new Mat();
    Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

    // 使用 Gaussian Blur 模糊图片以减少噪声
    Mat blurredImage = new Mat();
    Imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 0);

    // 使用 Canny 边缘检测算法检测边界
    Mat edges = new Mat();
    Imgproc.Canny(blurredImage, edges, 100, 200);

    // 查找轮廓
    List<MatOfPoint> contours = new ArrayList<>();
    Mat hierarchy = new Mat();
    Imgproc.findContours(
        edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

    // 绘制轮廓
    Mat contourImage = new Mat();
    image.copyTo(contourImage);
    Imgproc.drawContours(contourImage, contours, -1, new Scalar(0, 255, 0), 2);

    // 显示结果
    Imgcodecs.imwrite("/Users/yuxinbai/Desktop/output0.jpg", contourImage);
    System.out.println("边界分析完成，结果已保存到 output0.jpg");
  }
}
