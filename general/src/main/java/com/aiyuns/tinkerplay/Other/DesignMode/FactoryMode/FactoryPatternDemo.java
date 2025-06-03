package com.aiyuns.tinkerplay.Other.DesignMode.FactoryMode;

/**
 * @Author: aiYunS @Date: 2023年9月28日, 0028 上午 9:29:11 @Description: 使用图形工厂类来创建不同类型的形状对象
 */
public class FactoryPatternDemo {
  public static void main(String[] args) {
    ShapeFactory shapeFactory = new ShapeFactory();

    // 创建圆形对象
    Shape circle = shapeFactory.createShape("CIRCLE");
    circle.draw();

    // 创建矩形对象
    Shape rectangle = shapeFactory.createShape("RECTANGLE");
    rectangle.draw();
  }
}
