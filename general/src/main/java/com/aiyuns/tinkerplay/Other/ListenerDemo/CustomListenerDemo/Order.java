package com.aiyuns.tinkerplay.Other.ListenerDemo.CustomListenerDemo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: aiYunS @Date: 2023年11月6日, 0006 上午 11:07:15 @Description: 订单类 触发订单事件
 */
public class Order {
  private String orderNumber;
  private String status;
  private List<OrderListener> listeners = new ArrayList<>();

  public Order(String orderNumber) {
    this.orderNumber = orderNumber;
    this.status = "新订单";
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
    notifyListeners();
  }

  public void addOrderListener(OrderListener listener) {
    listeners.add(listener);
  }

  public void removeOrderListener(OrderListener listener) {
    listeners.remove(listener);
  }

  private void notifyListeners() {
    OrderEvent event = new OrderEvent(this, orderNumber, status);
    for (OrderListener listener : listeners) {
      listener.orderStatusChanged(event);
    }
  }
}
