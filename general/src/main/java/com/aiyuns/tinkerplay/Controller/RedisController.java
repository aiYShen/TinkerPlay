package com.aiyuns.tinkerplay.Controller;

import com.aiyuns.tinkerplay.Common.CommonResult;
import com.aiyuns.tinkerplay.Controller.Service.RedisService;
import com.aiyuns.tinkerplay.CustomAnnotations.WebLog;
import com.aiyuns.tinkerplay.Entity.Sms;
import com.aiyuns.tinkerplay.Rabbitmq.BroadcastMessageSender;
import com.aiyuns.tinkerplay.Utils.SendSmsUtil;
import com.aiyuns.tinkerplay.Utils.UnicodeUtil;
import com.github.qcloudsms.SmsSingleSenderResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: aiYunS @Date: 2023年3月9日, 0009 下午 4:48:18 @Description: 测试操作redis Service
 */
@RestController
@RequestMapping(value = "RedisController", produces = "application/json;charset=UTF-8")
@EnableScheduling // 1.开启定时任务
@EnableAsync // 2.开启多线程
@Tag(name = "RedisController", description = "Redis操作模块")
public class RedisController {

  @Autowired RedisService redisService;
  @Autowired BroadcastMessageSender broadcastMessageSender;

  @Operation(summary = "测试Redis_Service")
  @WebLog(description = "测试Redis_Service")
  @RequestMapping(value = "/redis", method = RequestMethod.GET)
  public void redis() {
    redisService.set("A", "a");
    redisService.set("B", "b", 30);
    redisService.set("C", "c");
    System.out.println(redisService.get("A"));
    redisService.expire("C", 30);
    System.out.println(redisService.getExpire("C"));
    redisService.del("A");
  }

  /***
   * @Author: aiYunS
   * @Description: 测试缓存,手机发送验证码,异步广播消息
   * @Date: 2023年6月15日, 0015 上午 11:00:32
   * @Param:
   * @param sms
   * @return: com.aiyuns.tinkerplay.Common.CommonResult
   */
  @Operation(summary = "测试获取验证码并发送到手机")
  @WebLog(description = "测试获取验证码并发送到手机")
  @RequestMapping(
      value = "/getAuthCode",
      method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public CommonResult getAuthCode(@RequestBody Sms sms) {
    if (!redisService.hasKey(sms.getPhoneNumbers()[0])) {
      // 生成验证码
      StringBuilder sb = new StringBuilder();
      Random random = new Random();
      for (int i = 0; i < 6; i++) {
        sb.append(random.nextInt(10));
      }
      String authCode = sb.toString();
      // 发送验证码
      String[] params = new String[1];
      params[0] = authCode;
      SmsSingleSenderResult result =
          SendSmsUtil.sendSms(sms.getPhoneNumbers(), sms.getTemplateId(), sms.getSmsSign(), params);
      if (StringUtils.isNotBlank(result.errMsg)) {
        String errmsg = UnicodeUtil.unicodeToString(result.errMsg);
        return CommonResult.failed(errmsg);
      }
      // 新起线程存到redis缓存,默认60s超时
      new Thread(
              () -> {
                redisService.set(sms.getPhoneNumbers()[0], sb, 60);
              })
          .start();
      // 异步广播
      System.out.println("开始发送异步广播消息");
      CompletableFuture.runAsync(
          () ->
              broadcastMessageSender.sendBroadcastMessage("我是广播消息! 验证码为: " + authCode, 60 * 1000));
      return CommonResult.success(authCode, "获取验证码成功");
    }
    return CommonResult.success(null, "请一分钟后重新获取验证码!");
  }
}
