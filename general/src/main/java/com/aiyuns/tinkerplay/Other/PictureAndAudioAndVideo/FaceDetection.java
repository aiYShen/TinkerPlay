package com.aiyuns.tinkerplay.Other.PictureAndAudioAndVideo;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

// 计算机视觉: 人脸识别
public class FaceDetection {

  static {
    // 加载 OpenCV 库
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  }

  public static void main(String[] args) {

    // 加载人脸检测模型文件（haarcascade_frontalface_default.xml）
    String cascadeFilePath =
        "/usr/local/share/opencv4/haarcascades/haarcascade_frontalface_default.xml";
    CascadeClassifier faceDetector = new CascadeClassifier(cascadeFilePath);

    // 读取图像文件
    String imagePath = "/Users/yuxinbai/Desktop/face.jpg";
    Mat image = Imgcodecs.imread(imagePath);

    // 将图像转换为灰度图像
    Mat grayImage = new Mat();
    Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

    // 进行人脸检测
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(grayImage, faceDetections);

    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

    // 在原始图像上绘制人脸矩形框
    for (Rect rect : faceDetections.toArray()) {
      Imgproc.rectangle(
          image,
          new Point(rect.x, rect.y),
          new Point(rect.x + rect.width, rect.y + rect.height),
          new Scalar(0, 255, 0),
          3);
    }

    // 保存检测结果图像
    Imgcodecs.imwrite("/Users/yuxinbai/Desktop/output.jpg", image);

    System.out.println("人脸识别完成，结果已保存到 output.jpg");
  }
}
