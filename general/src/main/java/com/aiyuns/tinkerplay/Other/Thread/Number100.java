package com.aiyuns.tinkerplay.Other.Thread;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: aiYunS @Date: 2022-12-21 9:14 @Description: 用五个线程实现，求123456789
 * 之间放+-和100的表达式，如果一个线程求出结果，立即告诉其它停止
 */
public class Number100 {

  // 原子类, 保证原子性
  AtomicBoolean atomicBoolean = new AtomicBoolean(true);

  public void show() {
    String[] ss = {"", "+", "-"};
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("1");
    Random random = new Random();
    while (atomicBoolean.get()) {
      for (int i = 2; i < 9; i++) {
        stringBuilder.append(ss[random.nextInt(3)]);
        stringBuilder.append(i);
      }
      // 预编译正则表达式为模式对象
      Pattern pattern = Pattern.compile("[0-9]+|-[0-9]+");
      // 通过模式对象得到匹配器对象
      Matcher matcher = pattern.matcher(stringBuilder.toString());
      int sum = 0;
      while (matcher.find()) {
        sum += Integer.parseInt(matcher.group());
      }
      if (sum == 100) {
        atomicBoolean.set(false);
        System.out.println(
            Thread.currentThread().getName() + ":" + stringBuilder.toString() + " = 100");
      }
      stringBuilder.delete(1, stringBuilder.length());
    }
  }

  public static void main(String[] args) {
    Number100 n = new Number100();
    for (int i = 0; i < 5; i++) {
      new Thread(n::show).start();
    }
  }
}
