package com.aiyuns.tinkerplay.Utils;

import com.aiyuns.tinkerplay.Other.MySmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import java.io.IOException;
import org.json.JSONException;

/**
 * @Author: aiYunS @Date: 2023年6月1日, 0001 上午 10:14:05 @Description: 使用腾讯云三方包发送手机短信
 */
public class SendSmsUtil {

  /**
   * @Author: aiYunS @Description: 发送短信 @Date: 2023年6月1日, 0001 上午 10:55:02 @Param:
   *
   * @param phoneNumbers 手机号
   * @param templateId 短信模板ID
   * @param smsSign 签名
   * @param params 模板参数
   * @return: void
   */
  public static SmsSingleSenderResult sendSms(
      String[] phoneNumbers, Integer templateId, String smsSign, String[] params) {
    try {
      if (phoneNumbers == null || phoneNumbers.length == 0) {
        throw new RuntimeException("手机号不能为空!");
      }
      if (templateId == null || templateId == 0) {
        throw new RuntimeException("短信模板ID不能为空!");
      }
      if (smsSign == null || "".equals(smsSign) || smsSign.length() == 0) {
        throw new RuntimeException("签名模板不能为空!");
      }
      // 数组具体的元素个数和模板中变量个数必须一致，例如示例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
      if (params == null && params.length == 0) {
        throw new RuntimeException("短信参数不能为空!");
      }
      // 签名参数未提供或者为空时，会使用默认签名发送短信
      MySmsSingleSender ssender =
          new MySmsSingleSender(1400826667, "1666486b3e054c09db4fd7e6a7b94a51");
      SmsSingleSenderResult result =
          ssender.sendWithParam("86", phoneNumbers[0], templateId, params, smsSign, "", "");
      return result;
    } catch (HTTPException e) {
      // HTTP响应码错误
      e.printStackTrace();
    } catch (JSONException e) {
      // json解析错误
      e.printStackTrace();
    } catch (IOException e) {
      // 网络IO错误
      e.printStackTrace();
    }
    return null;
  }
}
