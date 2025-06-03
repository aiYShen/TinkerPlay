package com.aiyuns.tinkerplay.Other.ListenerDemo.CustomListenerDemo;

import java.util.EventObject;

/**
 * @Author: aiYunS @Date: 2023年11月6日, 0006 上午 11:05:29 @Description: OrderEvent 类来表示订单事件
 */
public class OrderEvent extends EventObject {
  private String orderNumber;
  private String status;

  public OrderEvent(Object source, String orderNumber, String status) {
    super(source);
    this.orderNumber = orderNumber;
    this.status = status;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public String getStatus() {
    return status;
  }
}
