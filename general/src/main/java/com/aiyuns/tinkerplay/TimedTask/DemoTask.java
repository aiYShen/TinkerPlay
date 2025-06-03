package com.aiyuns.tinkerplay.TimedTask;

import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2022-10-10 上午 11:13 @Description: 定时任务示例类
 */
@Component("demoTask")
public class DemoTask {
  public void taskWithParams(String params) {
    System.out.println("执行有参示例任务：" + params);
  }

  public void taskNoParams() {
    System.out.println("执行无参示例任务");
  }
}
