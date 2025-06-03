package com.aiyuns.tinkerplay.Rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2023年6月15日, 0015 上午 10:33:23 @Description: MQ广播消息发送者
 */
@Component
public class BroadcastMessageSender {

  @Autowired private RabbitTemplate rabbitTemplate;

  public void sendBroadcastMessage(String message, final long delayTimes) {
    rabbitTemplate.convertAndSend("exchange_broadcast", "", message);
    System.out.println("广播消息发送完成!");
  }
}
