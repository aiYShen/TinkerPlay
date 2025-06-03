package com.aiyuns.tinkerplay.Rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2023年6月15日, 0015 上午 10:35:57 @Description: MQ广播消息接收者
 */
@Component
public class BroadcastMessageReceiver {
  @RabbitListener(queues = "queue_broadcast")
  public void receiveBroadcastMessage(String message) {
    // 处理接收到的广播消息
    System.out.println("接收到广播消息了: " + message);
  }
}
