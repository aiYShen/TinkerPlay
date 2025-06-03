package com.aiyuns.tinkerplay.Utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: aiYunS @Date: 2022-7-14 上午 10:59 @Description: 行转列工具类
 */
public class RowConvertColUtil {

  private static final SimpleDateFormat sdf =
      new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

  private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);

  private static Set<Object> headerSet;
  private static Set<Object> fixedColumnSet;

  public RowConvertColUtil() {}

  public static class ConvertData {
    private Set<Object> headerSet;
    private Set<Object> fixedColumnSet;
    private List<List<Object>> dataList;

    public ConvertData(
        List<List<Object>> dataList, Set<Object> headerSet, Set<Object> fixedColumnSet) {
      this.headerSet = headerSet;
      this.fixedColumnSet = fixedColumnSet;
      this.dataList = dataList;
    }

    public Set<Object> getHeaderSet() {
      return headerSet;
    }

    public void setHeaderSet(Set<Object> headerSet) {
      this.headerSet = headerSet;
    }

    public Set<Object> getfixedColumnSet() {
      return fixedColumnSet;
    }

    public void setfixedColumnSet(Set<Object> fixedColumnSet) {
      this.fixedColumnSet = fixedColumnSet;
    }

    public List<List<Object>> getDataList() {
      return dataList;
    }

    public void setDataList(List<List<Object>> dataList) {
      this.dataList = dataList;
    }
  }

  /**
   * 行转列,返回ConvertData
   *
   * @param orignalList 要行转列的List
   * @param headerName 要行转列的字段
   * @param fixedColumn 固定列字段
   * @param valueFiedName 行转列字段对应值列的字段名
   * @param needHeader 是否返回表头
   * @param fixedColumnName 固定列字段名称数组
   * @param nullValue 定义空值补数
   * @return ConvertData
   */
  public static synchronized ConvertData doConvertReturnObj(
      List orignalList,
      String headerName,
      String[] fixedColumn,
      String valueFiedName,
      boolean needHeader,
      String[] fixedColumnName,
      String nullValue)
      throws Exception {
    List<List<Object>> lists =
        doConvert(
            orignalList,
            headerName,
            fixedColumn,
            valueFiedName,
            needHeader,
            fixedColumnName,
            nullValue);
    return new ConvertData(lists, headerSet, fixedColumnSet);
  }

  /**
   * 行转列,返回转换后的list
   *
   * @param orignalList 要行转列的List
   * @param headerName 要行转列的字段
   * @param fixedColumn 固定列字段
   * @param valueFiedName 行转列字段对应值列的字段名
   * @param needHeader 是否返回表头
   * @param fixedColumnName 固定列字段名称数组
   * @param nullValue 定义空值补数
   */
  public static synchronized List<List<Object>> doConvert(
      List orignalList,
      String headerName,
      String[] fixedColumn,
      String valueFiedName,
      boolean needHeader,
      String[] fixedColumnName,
      String nullValue)
      throws Exception {
    headerSet = new LinkedHashSet<>();
    fixedColumnSet = new LinkedHashSet<>();
    // resultList:首行名称list
    List<List<Object>> resultList = new ArrayList<>();

    getHeaderfixedColumnSet(orignalList, headerName, fixedColumn);
    if (needHeader) {
      List<Object> headerList = new ArrayList<>();
      // 填充进header
      for (String value : fixedColumnName) {
        headerList.add(value);
      }
      headerList.addAll(headerSet);
      resultList.add(headerList);
    }
    for (Object fixedColumnItem : fixedColumnSet) {
      // colList：数据list
      List<Object> colList = new ArrayList<>();
      // 名称
      for (String ColNameItem : fixedColumnItem.toString().split("\\|")) {
        if (isValid(ColNameItem)) {
          colList.add(sdf2.format(sdf.parse(ColNameItem)));
        } else {
          colList.add(ColNameItem);
        }
      }
      for (Object headerItem : headerSet) {
        boolean flag = true;
        for (Object orignalObjectItem : orignalList) {
          Field fixedColumnField = null;
          Field headerField = orignalObjectItem.getClass().getDeclaredField(headerName);
          headerField.setAccessible(true);
          fixedColumnField = orignalObjectItem.getClass().getDeclaredField(fixedColumn[0]);
          fixedColumnField.setAccessible(true);
          Field valueField = orignalObjectItem.getClass().getDeclaredField(valueFiedName);
          valueField.setAccessible(true);
          if (headerItem.equals(headerField.get(orignalObjectItem))) {
            if (fixedColumnItem
                .toString()
                .split("\\|")[0]
                .equals(fixedColumnField.get(orignalObjectItem))) {
              colList.add(valueField.get(orignalObjectItem));
              flag = false;
              break;
            }
          }
        }
        if (flag) {
          colList.add(nullValue);
        }
      }
      resultList.add(colList);
    }
    return resultList;
  }

  private static void getHeaderfixedColumnSet(
      List orignalList, String headerName, String[] fixedColumn) {
    try {
      for (Object item : orignalList) {

        Field headerField = item.getClass().getDeclaredField(headerName);
        headerField.setAccessible(true);
        headerSet.add(headerField.get(item));
        StringBuffer sBuffer = new StringBuffer();
        int len = 1;
        for (String name : fixedColumn) {
          Field fixedColumnField = item.getClass().getDeclaredField(name);
          fixedColumnField.setAccessible(true);
          sBuffer.append(fixedColumnField.get(item));
          if (len < fixedColumn.length) {
            sBuffer.append("|");
          }
          len++;
        }
        fixedColumnSet.add(sBuffer.toString());
      }
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public static boolean isValid(String dateStr) {
    try {
      Date parsedDate = sdf.parse(dateStr);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }
}
