package com.aiyuns.tinkerplay.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Http工具类
 *
 * <p>1、发送GET请求；
 *
 * <p>2、发送POST请求。
 */
public class HttpUtil {
  public static String doGet(String url) {

    CloseableHttpClient httpclient = HttpClients.createDefault();
    CloseableHttpResponse response = null;
    HttpGet request = new HttpGet(url);

    // 爬虫伪装成浏览,设置成浏览器的请求头信息
    request.setHeader(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
            + " Chrome/107.0.0.0 Safari/537.36");
    //
    //        //判断入参url是地址还是ip
    //        boolean isIP = isCorrectIp(url);
    //        if(!isIP){
    //            HttpHost proxy = new HttpHost("121.13.252.62",41564);
    //            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
    //            request.setConfig(config);
    //        }

    try {
      // HttpGet httpget = new HttpGet(url);
      // System.out.println("executing request " + httpget.getURI());
      // 执行get请求.
      response = httpclient.execute(request);
      try {
        // 获取响应实体
        HttpEntity entity = response.getEntity();
        System.out.println("--------------------------------------");
        // 打印响应状态
        System.out.println(response.getStatusLine());
        if (entity != null) {
          return EntityUtils.toString(entity);
        }
        System.out.println("------------------------------------");
      } finally {
        response.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 关闭连接,释放资源
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /** post方式提交表单（模拟用户登录请求） */
  public static String postForm(String url, Map<String, String> params) {
    // 创建默认的httpClient实例.
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httppost = new HttpPost(url);
    // 创建参数队列
    List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    }
    UrlEncodedFormEntity uefEntity;
    try {
      uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
      httppost.setEntity(uefEntity);
      System.out.println("executing request " + httppost.getURI());
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          return EntityUtils.toString(entity, "UTF-8");
        }
      } finally {
        response.close();
      }
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // 关闭连接,释放资源
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  // 判断入参是地址还是IP
  public static boolean isCorrectIp(String ipString) {
    // IP地址的正则表达式
    String ipRegex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
    // 如果前三项判断都满足，就判断每段数字是否都位于0-255之间
    if (ipString.matches(ipRegex)) {
      String[] ipArray = ipString.split("\\.");
      for (int i = 0; i < ipArray.length; i++) {
        int number = Integer.parseInt(ipArray[i]);
        // 4.判断每段数字是否都在0-255之间
        if (number < 0 || number > 255) {
          return false;
        }
      }
      return true;
    } else {
      // 如果与正则表达式不匹配，则返回false
      return false;
    }
  }
}
