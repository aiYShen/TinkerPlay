package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月29日, 0029 下午 7:41:00 @Description: 实现一个方法，给定订购的年月日 (year, month,
 * day)，已知订购时长是一个月，请返回订单的到期日。year, month, day 均为整数，输入已保证是正确的日期
 */
public class Main21 {

  public static String getExpirationDate(int year, int month, int day) {

    // 到期年月日
    int newYear = year;
    int newMonth = month;
    int newDay = day;

    // 每月最大天数
    int[] maxDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // 如果是12月,年份+1,月份为1;反之月份+1
    if (month == 12) {
      newYear += 1;
      newMonth = 1;
    } else {
      newMonth += 1;
    }

    // 是否闰年
    boolean isLeapYear;
    // 判断是不是闰年
    if (year % 4 == 0) {
      if (year % 100 == 0) {
        if (year % 400 == 0) {
          isLeapYear = true;
        } else {
          isLeapYear = false;
        }
      } else {
        isLeapYear = true;
      }
    } else {
      isLeapYear = false;
    }
    // 闰年的2月是29天
    if (isLeapYear && newMonth == 2) {
      maxDays[1] = 29;
    }

    // 检查到期月份天数是否合法
    if (newDay > maxDays[newMonth - 1]) {
      newDay = maxDays[newMonth - 1];
    }

    return newYear + "-" + newMonth + "-" + newDay;
  }

  public static void main(String[] args) {

    int year = 2008;
    int month = 1;
    int day = 31;
    String date = getExpirationDate(year, month, day);
    System.out.println("订购日期: " + year + "-" + month + "-" + day + " 订单到期日：" + date);

    int year1 = 2008;
    int month1 = 12;
    int day1 = 15;
    String date1 = getExpirationDate(year1, month1, day1);
    System.out.println("订购日期: " + year1 + "-" + month1 + "-" + day1 + " 订单到期日：" + date1);

    int year2 = 2023;
    int month2 = 1;
    int day2 = 31;
    String date2 = getExpirationDate(year2, month2, day2);
    System.out.println("订购日期: " + year2 + "-" + month2 + "-" + day2 + " 订单到期日：" + date2);

    int year3 = 2023;
    int month3 = 05;
    int day3 = 31;
    String date3 = getExpirationDate(year3, month3, day3);
    System.out.println("订购日期: " + year3 + "-" + month3 + "-" + day3 + " 订单到期日：" + date3);

    int year4 = 2023;
    int month4 = 04;
    int day4 = 30;
    String date4 = getExpirationDate(year4, month4, day4);
    System.out.println("订购日期: " + year4 + "-" + month4 + "-" + day4 + " 订单到期日：" + date4);
  }
}
