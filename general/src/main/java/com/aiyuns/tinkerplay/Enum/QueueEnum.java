package com.aiyuns.tinkerplay.Enum;

import lombok.Getter;

/**
 * @Author: aiYunS @Date: 2023年2月17日, 0017 上午 10:49:07 @Description: 消息队列枚举配置
 */
@Getter
public enum QueueEnum {
  /** 消息通知队列 */
  EXCHANGE_QUEUE_Aa("exchange.baiyx.A", "queue.baiyx.a", "exchange.queue.Aa"),
  /** 消息通知ttl队列 */
  EXCHANGE_QUEUE_Bb("exchange.baiyx.B", "queue.baiyx.b", "exchange.baiyx.Bb");

  /** 交换名称 */
  private String exchange;

  /** 队列名称 */
  private String name;

  /** 路由键 */
  private String routeKey;

  QueueEnum(String exchange, String name, String routeKey) {
    this.exchange = exchange;
    this.name = name;
    this.routeKey = routeKey;
  }
}
