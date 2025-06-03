package com.aiyuns.tinkerplay.Other.ListenerDemo.CustomListenerDemo;

/**
 * @Author: aiYunS @Date: 2023年11月6日, 0006 上午 11:10:26 @Description: 自定义监听器
 */
public class CustomListenerDemo {

  public static void main(String[] args) {
    Order order = new Order("12345");

    // 创建监听器并将其添加到订单对象
    OrderListener listener1 =
        new OrderListener() {
          @Override
          public void orderStatusChanged(OrderEvent event) {
            System.out.println("监听器1：订单 " + event.getOrderNumber() + " 状态变为 " + event.getStatus());
          }
        };
    order.addOrderListener(listener1);

    // 修改订单状态，触发监听器
    order.setStatus("已发货");

    // 移除监听器
    order.removeOrderListener(listener1);

    // 创建另一个监听器
    OrderListener listener2 =
        new OrderListener() {
          @Override
          public void orderStatusChanged(OrderEvent event) {
            System.out.println("监听器2：订单 " + event.getOrderNumber() + " 状态变为 " + event.getStatus());
          }
        };
    order.addOrderListener(listener2);

    // 再次修改订单状态，触发新的监听器
    order.setStatus("已签收");
  }
}
