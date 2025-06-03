package com.aiyuns.tinkerplay.Other.DesignMode.FactoryMode;

/**
 * @Author: aiYunS @Date: 2023年9月28日, 0028 上午 9:25:12 @Description: 工厂模式: 定义图形工厂类
 */
public class ShapeFactory {
  public Shape createShape(String shapeType) {
    if (shapeType == null) {
      return null;
    }
    if ("CIRCLE".equalsIgnoreCase(shapeType)) {
      return new Circle();
    } else if ("RECTANGLE".equalsIgnoreCase(shapeType)) {
      return new Rectangle();
    }
    return null;
  }
}
