package com.aiyuns.tinkerplay.Other.ListenerDemo.CustomListenerDemo;

/**
 * @Author: aiYunS @Date: 2023年11月6日, 0006 上午 11:04:28 @Description: 自定义监听器接口 OrderListener，定义监听器的方法
 */
public interface OrderListener {
  void orderStatusChanged(OrderEvent event);
}
