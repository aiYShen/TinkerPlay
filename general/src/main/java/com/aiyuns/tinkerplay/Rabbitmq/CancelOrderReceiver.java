package com.aiyuns.tinkerplay.Rabbitmq;

import com.aiyuns.tinkerplay.Controller.Service.UserService;
import com.aiyuns.tinkerplay.Entity.QueryRequestVo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2022-9-1 下午 03:31 @Description: 消息的接收者CancelOrderReceiver 消息的处理者
 */
@Component
@RabbitListener(queues = "queue.baiyx.a") // 监听key为queue.baiyx.a的队列
public class CancelOrderReceiver {

  private static Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);
  @Autowired private UserService userService;

  @RabbitHandler
  public void handle(String queryRequestVo) {
    LOGGER.info("receive delay message username:{}", queryRequestVo);
    // 复制了userService.deleteByName的方法 用来deleteByNam2测试延迟执行
    System.out.println("开始执行延迟删除数据");
    userService.deleteByName2(
        JSON.parseObject(queryRequestVo).getObject("QueryRequestVo", QueryRequestVo.class));
    System.out.println("执行延迟删除数据完成");
  }
}
