package com.aiyuns.tinkerplay.Other.DesignMode.FactoryMode;

/**
 * @Author: aiYunS @Date: 2023年9月28日, 0028 上午 9:23:44 @Description: 工厂模式: 定义图形接口的实现类矩形类
 */
public class Rectangle implements Shape {
  @Override
  public void draw() {
    System.out.println("画出了一个矩形!");
  }
}
