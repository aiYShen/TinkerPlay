package com.aiyuns.tinkerplay.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: aiYunS @Date: 2024年2月24日, 0024 下午 11:03:27 @Description: Unicode转中文, 中文转Unicode
 */
public class UnicodeUtil {

  public static String unicodeToString(String unicodeStr) {
    StringBuilder sb = new StringBuilder();
    int length = unicodeStr.length();
    for (int i = 0; i < length; ) {
      if (unicodeStr.charAt(i) == '\\') {
        if (i + 6 <= length && unicodeStr.charAt(i + 1) == 'u') {
          String hex = unicodeStr.substring(i + 2, i + 6);
          char c = (char) Integer.parseInt(hex, 16);
          sb.append(c);
          i += 6;
        } else {
          sb.append(unicodeStr.charAt(i));
          i++;
        }
      } else {
        sb.append(unicodeStr.charAt(i));
        i++;
      }
    }
    return sb.toString();
  }

  public static String stringToUnicode(String str) {
    StringBuilder sb = new StringBuilder();
    for (char c : str.toCharArray()) {
      sb.append("\\u").append(Integer.toHexString(c));
    }
    return sb.toString();
  }

  public static boolean containsChinese(String str) {
    Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
    Matcher matcher = pattern.matcher(str);
    return matcher.find();
  }
}
