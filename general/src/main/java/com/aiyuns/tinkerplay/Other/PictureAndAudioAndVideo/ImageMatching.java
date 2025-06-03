package com.aiyuns.tinkerplay.Other.PictureAndAudioAndVideo;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;

// 计算机视觉: 图像匹配
public class ImageMatching {

  static {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  }

  public static void main(String[] args) {
    // 读取图像
    Mat image1 = Imgcodecs.imread("/Users/yuxinbai/Desktop/face.jpg", Imgcodecs.IMREAD_GRAYSCALE);
    Mat image2 = Imgcodecs.imread("/Users/yuxinbai/Desktop/face1.jpg", Imgcodecs.IMREAD_GRAYSCALE);

    // 创建 SIFT 特征点检测器
    SIFT detector = SIFT.create();

    // 检测特征点并计算描述符
    MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
    Mat descriptors1 = new Mat();
    detector.detectAndCompute(image1, new Mat(), keypoints1, descriptors1);

    MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
    Mat descriptors2 = new Mat();
    detector.detectAndCompute(image2, new Mat(), keypoints2, descriptors2);

    // 创建 FlannBasedMatcher 匹配器
    FlannBasedMatcher matcher = FlannBasedMatcher.create();

    // 进行匹配
    MatOfDMatch matches = new MatOfDMatch();
    matcher.match(descriptors1, descriptors2, matches);

    // 筛选匹配结果 (可选)
    List<DMatch> goodMatches = new ArrayList<>();
    for (DMatch match : matches.toList()) {
      if (match.distance < 0.75 * matches.toList().get(0).distance) {
        goodMatches.add(match);
      }
    }

    // 绘制匹配结果
    Mat outputImage = new Mat();
    Features2d.drawMatches(
        image1,
        keypoints1,
        image2,
        keypoints2,
        new MatOfDMatch(goodMatches.toArray(new DMatch[0])),
        outputImage);

    // 显示匹配结果
    Imgcodecs.imwrite("/Users/yuxinbai/Desktop/output1.jpg", outputImage);
  }
}
