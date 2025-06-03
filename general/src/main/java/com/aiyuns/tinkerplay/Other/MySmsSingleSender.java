package com.aiyuns.tinkerplay.Other;

import com.alibaba.fastjson.JSONObject;
import com.github.qcloudsms.SmsSenderUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

/**
 * @Author: aiYunS @Date: 2023年6月13日, 0013 上午 10:25:59 @Description:
 * 腾讯云引用json的包与spring-boot有冲突，故自己来实现
 */
public class MySmsSingleSender extends SmsSingleSender {
  private String url = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms";

  public MySmsSingleSender(int appid, String appkey) {
    super(appid, appkey);
  }

  public MySmsSingleSender(int appid, String appkey, HTTPClient httpclient) {
    super(appid, appkey, httpclient);
  }

  @Override
  public SmsSingleSenderResult sendWithParam(
      String nationCode,
      String phoneNumber,
      int templateId,
      String[] params,
      String sign,
      String extend,
      String ext)
      throws HTTPException, JSONException, IOException {
    return this.sendWithParam(
        nationCode,
        phoneNumber,
        templateId,
        new ArrayList(Arrays.asList(params)),
        sign,
        extend,
        ext);
  }

  @Override
  public SmsSingleSenderResult sendWithParam(
      String nationCode,
      String phoneNumber,
      int templateId,
      ArrayList<String> params,
      String sign,
      String extend,
      String ext)
      throws HTTPException, JSONException, IOException {

    long random = SmsSenderUtil.getRandom();
    long now = SmsSenderUtil.getCurrentTime();
    // JSONObject body = (new JSONObject()).put("tel", (new JSONObject()).put("nationcode",
    // nationCode).put("mobile", phoneNumber)).put("sig",
    // SmsSenderUtil.calculateSignature(this.appkey, random, now, phoneNumber)).put("tpl_id",
    // templateId).put("params", params).put("sign", sign).put("time", now).put("extend",
    // SmsSenderUtil.isNotEmpty(extend)?extend:"").put("ext", SmsSenderUtil.isNotEmpty(ext)?ext:"");
    // 扩展方法 使用fastjson
    /**
     * 入参
     *
     * <p>{ "ext": "", "extend": "", "params": [ "验证码", "1234", "4" ], "sig":
     * "ecab4881ee80ad3d76bb1da68387428ca752eb885e52621a3129dcf4d9bc4fd4", "sign": "腾讯云", "tel": {
     * "mobile": "13788888888", "nationcode": "86" }, "time": 1457336869, "tpl_id": 19 }
     */
    JSONObject body = new JSONObject();
    Map telMap = new HashMap<>();
    telMap.put("mobile", phoneNumber);
    telMap.put("nationcode", nationCode);
    body.put("tel", telMap);
    body.put("params", params);
    body.put("tpl_id", templateId);
    body.put("sig", SmsSenderUtil.calculateSignature(this.appkey, random, now, phoneNumber));
    body.put("sign", sign);
    body.put("time", now);
    body.put("extend", SmsSenderUtil.isNotEmpty(extend) ? extend : "");
    body.put("ext", SmsSenderUtil.isNotEmpty(ext) ? ext : "");

    HTTPRequest req =
        (new HTTPRequest(HTTPMethod.POST, this.url))
            .addHeader("Conetent-Type", "application/json")
            .addQueryParameter("sdkappid", this.appid)
            .addQueryParameter("random", random)
            .setConnectionTimeout('\uea60')
            .setRequestTimeout('\uea60')
            .setBody(body.toString());

    try {
      HTTPResponse e = this.httpclient.fetch(req);
      this.handleError(e);
      return (new SmsSingleSenderResult()).parseFromHTTPResponse(e);
    } catch (URISyntaxException var15) {
      throw new RuntimeException("API url has been modified, current url: " + this.url);
    }
  }
}
