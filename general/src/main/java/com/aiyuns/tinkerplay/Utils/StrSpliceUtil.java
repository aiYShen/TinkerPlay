package com.aiyuns.tinkerplay.Utils;

import java.util.ArrayList;

/**
 * @Author: aiYunS @Date: 2022-9-22 下午 04:18 @Description: 字符串拼接工具类
 */
public class StrSpliceUtil {

  // 数组元素
  public static String strSplice(ArrayList arrayList) {
    String str = "";
    StringBuffer stringBuffer = new StringBuffer();
    if (arrayList.size() > 1) {
      for (int i = 0; i < arrayList.size(); i++) {
        if (i == arrayList.size() - 1) {
          stringBuffer.append(arrayList.get(i).toString());
        } else {
          stringBuffer.append(arrayList.get(i).toString());
          stringBuffer.append(", ");
        }
      }
      str = stringBuffer.toString();
    } else if (arrayList.size() == 1) {
      str = arrayList.get(0).toString();
    } else {
      return str;
    }
    return str;
  }
}
