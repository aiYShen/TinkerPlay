package com.aiyuns.tinkerplay.Utils;

import static org.unbescape.java.JavaEscape.unescapeJava;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

/**
 * @Author: aiYunS @Date: 2022-9-15 下午 05:22 @Description: 读取txt文件内容,转为JSONObject工具类
 */
public class ReadTXTtoJsonObjUtil {

  private static String FILEPATH = "D:/Users/lenovo/Desktop/jsonObj.txt";

  public static JSONObject[] readTXTtoObj(String filePath) {
    if (Objects.equals(filePath, "") || filePath == null) {
      filePath = ReadTXTtoJsonObjUtil.FILEPATH;
    }
    StringBuilder result = new StringBuilder();
    try {
      // 构造一个BufferedReader类来读取文件
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      String s = null;
      // 使用readLine方法，一次读一行
      while ((s = br.readLine()) != null) {
        result.append(System.lineSeparator()).append(s);
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return stringToObj(result.toString());
  }

  public static JSONObject[] stringToObj(String s) {
    s = s.replaceAll("\r|\n|\\s*", "");
    JSONObject jsonObject = null;
    JSONObject[] jsonObjects = null;
    if (!s.isEmpty()) {
      String[] arr = s.split("=====aiYunS=====");
      jsonObjects = new JSONObject[arr.length];
      for (int i = 0; i < arr.length; i++) {
        String strObj = "";
        // JSON格式有问题的处理:长得截取,短得补齐
        if (arr[i].endsWith("\\\"suffInfoList\\\":[]}\"}")
            || arr[i].endsWith("\"suffInfoList\":[]}")) {
          strObj = arr[i];
        } else if (!arr[i].endsWith("\\\"suffInfoList\\\":[]}\"}")
            && arr[i].contains("\\\"suffInfoList\\\":[]}\"}")) {
          strObj =
              unescapeJava(arr[i].substring(0, arr[i].indexOf("\\\"suffInfoList\\\":[]}\"}") + 22));
        } else if (arr[i].contains("\"suffInfoList\":[]}")
            && !arr[i].endsWith("\"suffInfoList\":[]}")) {
          strObj = unescapeJava(arr[i].substring(0, arr[i].indexOf("\"suffInfoList\":[]}") + 19));
        } else if (!arr[i].endsWith("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"}")
            && arr[i].contains("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"}")) {
          strObj =
              unescapeJava(
                  arr[i].substring(
                      0, arr[i].indexOf("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"}") + 27));
        } else if (arr[i].contains("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"")
            && !arr[i].endsWith("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"")) {
          strObj = arr[i] + "}";
          strObj =
              unescapeJava(
                  strObj.substring(
                      0, strObj.indexOf("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"}") + 27));
        } else if (arr[i].contains("\\\\\\\"suffInfoList\\\\\\\":[]}")
            && arr[i].endsWith("\\\\\\\"suffInfoList\\\\\\\":[]}")) {
          strObj = arr[i] + "\\\"}";
          strObj =
              unescapeJava(
                  strObj.substring(
                      0, strObj.indexOf("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"}") + 27));
        } else if (arr[i].contains("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"")
            && arr[i].endsWith("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"")) {
          strObj = arr[i] + "}";
          strObj =
              unescapeJava(
                  strObj.substring(
                      0, strObj.indexOf("\\\\\\\"suffInfoList\\\\\\\":[]}\\\"}") + 27));
        } else if (arr[i].contains("\\\"suffInfoList\\\":[]}")
            && arr[i].endsWith("\\\"suffInfoList\\\":[]}")) {
          strObj = arr[i] + "\"}";
          strObj = unescapeJava(strObj);
        }
        if (strObj.endsWith("\\\"suffInfoList\\\":[]}\"}")) {
          strObj = unescapeJava(strObj);
        }
        if ("\"}".equals(strObj.substring(strObj.length() - 2, strObj.length()))
            && strObj.endsWith("\"suffInfoList\":[]}\"}")) {
          strObj = strObj.substring(0, strObj.length() - 2);
        }
        jsonObject = JSON.parseObject(strObj);
        jsonObjects[i] = jsonObject;
      }
    }
    return jsonObjects;
  }
}
