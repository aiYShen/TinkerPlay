package com.aiyuns.tinkerplay.Utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: aiYunS @Date: 2023年2月3日, 0003 下午 3:32:27 @Description: JDBC结果集转换List集合
 */
public class ResultSetToListUtil {

  public static List resultSetTolist(ResultSet rs) {
    List list = new ArrayList();
    try {
      // 获得结果集结构信息,元数据
      ResultSetMetaData md = rs.getMetaData();
      int columnCount = md.getColumnCount();
      ;
      while (rs.next()) {
        Map<String, Object> rowData = new HashMap<String, Object>();
        for (int i = 1; i <= columnCount; i++) {
          rowData.put(md.getColumnName(i), rs.getObject(i));
        }
        list.add(rowData);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }
}
