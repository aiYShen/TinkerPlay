package com.aiyuns.tinkerplay.Config;

import com.aiyuns.tinkerplay.Enum.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @Author: aiYunS
 * @Description: 用于配置交换机、队列及队列与交换机的绑定关系
 * @Date: 2022-9-1 下午 03:34
 */

@Configuration
public class RabbitMqConfig {
  /** 实际消费队列所绑定的交换机 */
  @Bean
  DirectExchange orderDirect() {
    return (DirectExchange)
        ExchangeBuilder.directExchange(QueueEnum.EXCHANGE_QUEUE_Aa.getExchange())
            // 持久化配置
            .durable(true)
            .build();
  }

  /** 延迟队列所绑定的交换机 */
  @Bean
  DirectExchange orderTtlDirect() {
    return (DirectExchange)
        ExchangeBuilder.directExchange(QueueEnum.EXCHANGE_QUEUE_Bb.getExchange())
            .durable(true)
            .build();
  }

  /** 实际消费队列 */
  @Bean
  public Queue orderQueue() {
    return new Queue(QueueEnum.EXCHANGE_QUEUE_Aa.getName());
  }

  /** 延迟队列（死信队列） */
  @Bean
  public Queue orderTtlQueue() {
    return QueueBuilder.durable(QueueEnum.EXCHANGE_QUEUE_Bb.getName())
        // 到期后转发的交换机
        .withArgument("x-dead-letter-exchange", QueueEnum.EXCHANGE_QUEUE_Aa.getExchange())
        // 到期后转发的路由键
        .withArgument("x-dead-letter-routing-key", QueueEnum.EXCHANGE_QUEUE_Aa.getRouteKey())
        .build();
  }

  /** 将队列绑定到交换机 */
  @Bean
  Binding orderBinding(DirectExchange orderDirect, Queue orderQueue) {
    return BindingBuilder.bind(orderQueue)
        .to(orderDirect)
        .with(QueueEnum.EXCHANGE_QUEUE_Aa.getRouteKey());
  }

  /** 将延迟队列绑定到交换机 */
  @Bean
  Binding orderTtlBinding(DirectExchange orderTtlDirect, Queue orderTtlQueue) {
    return BindingBuilder.bind(orderTtlQueue)
        .to(orderTtlDirect)
        .with(QueueEnum.EXCHANGE_QUEUE_Bb.getRouteKey());
  }

  // 广播交换机
  @Bean
  public FanoutExchange fanoutExchange() {
    return new FanoutExchange("exchange_broadcast");
  }

  // 广播队列
  @Bean
  public Queue queue() {
    return new Queue("queue_broadcast");
  }

  // 广播:交换机绑定队列
  @Bean
  public Binding binding(Queue queue, FanoutExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange);
  }
}
