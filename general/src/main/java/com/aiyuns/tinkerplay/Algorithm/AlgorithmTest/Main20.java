package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.time.LocalDate;
import java.util.*;

/**
 * @Author: aiYunS @Date: 2023年5月29日, 0029 下午 6:08:49 @Description:
 * 在真实的业务场景中，客户可以在任意一天订购我们的服务，订购周期可以是一个月、一个季度、半年或一年， 在订购日后的这么多月的当天凌晨 0 点订单到期,假定我们每天可以收到 4
 * 个订单，其中一个是一个月周期的，一个是一个季度周期的，一个是半年周期的，一个是一年周期的。 这样的订单从很久以前，比如五年前就开始了。在哪一天到期的订单会最多，有多少个？具体是哪几个订单？
 */
public class Main20 {

  public static void main(String[] args) {
    LocalDate currentDate = LocalDate.now();

    // 假设起始日期为当前日期的五年前
    LocalDate startDate = currentDate.minusYears(5);

    Map<LocalDate, Integer> result = new HashMap<>();

    // 遍历五年内的每一天
    for (LocalDate date = startDate; date.isBefore(currentDate); date = date.plusDays(1)) {
      // 计算订单周期到期的日期
      LocalDate monthlyOrderExpiry = date.plusMonths(1);
      LocalDate quarterlyOrderExpiry = date.plusMonths(3);
      LocalDate semiannualOrderExpiry = date.plusMonths(6);
      LocalDate annualOrderExpiry = date.plusYears(1);

      if ("2021-02-28".equals(monthlyOrderExpiry.toString())
          || "2021-02-28".equals(quarterlyOrderExpiry.toString())
          || "2021-02-28".equals(semiannualOrderExpiry.toString())
          || "2021-02-28".equals(annualOrderExpiry.toString())) {
        System.out.println(date);
      }

      if (result.containsKey(monthlyOrderExpiry)) {
        result.put(monthlyOrderExpiry, result.get(monthlyOrderExpiry) + 1);
      } else {
        result.put(monthlyOrderExpiry, 1);
      }

      if (result.containsKey(quarterlyOrderExpiry)) {
        result.put(quarterlyOrderExpiry, result.get(quarterlyOrderExpiry) + 1);
      } else {
        result.put(quarterlyOrderExpiry, 1);
      }

      if (result.containsKey(semiannualOrderExpiry)) {
        result.put(semiannualOrderExpiry, result.get(semiannualOrderExpiry) + 1);
      } else {
        result.put(semiannualOrderExpiry, 1);
      }

      if (result.containsKey(annualOrderExpiry)) {
        result.put(annualOrderExpiry, result.get(annualOrderExpiry) + 1);
      } else {
        result.put(annualOrderExpiry, 1);
      }
    }
    for (LocalDate key : result.keySet()) {
      System.out.println("key: " + key + " value: " + result.get(key));
    }
    // 找出到期订单数量最多的日期和订单数量
    // 将键值对转换为List
    List<Map.Entry<LocalDate, Integer>> entryList = new ArrayList<>(result.entrySet());
    // 按值排序
    Collections.sort(entryList, Comparator.comparing(Map.Entry::getValue));
    // 输出结果
    for (Map.Entry<LocalDate, Integer> entry : entryList) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }
  }
}
