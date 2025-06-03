package com.aiyuns.tinkerplay.Utils;

import com.aiyuns.tinkerplay.CustomAnnotations.excelRescoure;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : aiYunS @ClassName : ReadExcelUtil: Excel内容读取工具类
 * @date : 2022.10.12
 */
public class ReadExcelUtil {
  // 日志输出
  private static Logger logger = LoggerFactory.getLogger(ReadExcelUtil.class);
  // 定义excel类型
  private static final String XLS = "xls";
  private static final String XLSX = "xlsx";

  /**
   * 根据文件后缀名类型获取对应的工作簿对象
   *
   * @param inputStream 读取文件的输入流
   * @param fileType 文件后缀名类型（xls或xlsx）
   * @return 包含文件数据的工作簿对象
   */
  private static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
    Workbook workbook = WorkbookFactory.create(inputStream);
    // Workbook workbook = null;
    // if (fileType.equalsIgnoreCase(XLS)) {
    //    workbook = new HSSFWorkbook(inputStream);
    // } else if (fileType.equalsIgnoreCase(XLSX)) {
    //    workbook = new XSSFWorkbook(inputStream);
    // }
    return workbook;
  }

  /**
   * 将单元格内容转换为字符串
   *
   * @param cell
   * @return
   */
  private static String convertCellValueToString(Cell cell) {
    if (cell == null) {
      return null;
    }
    String returnValue = null;
    switch (cell.getCellType()) {
      // 数字
      case NUMERIC:
        Double doubleValue = cell.getNumericCellValue();
        // 格式化科学计数法，取一位整数，如取小数，值如0.0,取小数点后几位就写几个0
        DecimalFormat df = new DecimalFormat("0.00");
        returnValue = df.format(doubleValue);
        break;
      // 字符串
      case STRING:
        returnValue = cell.getStringCellValue();
        break;
      // 布尔
      case BOOLEAN:
        Boolean booleanValue = cell.getBooleanCellValue();
        returnValue = booleanValue.toString();
        break;
      // 空值
      case BLANK:
        break;
      // 公式
      case FORMULA:
        returnValue = cell.getCellFormula();
        break;
      // 故障
      case ERROR:
        break;
      default:
        break;
    }
    return returnValue;
  }

  /**
   * 处理Excel内容转为List<Map<String,Object>>输出 workbook：已连接的工作薄
   * StatrRow：读取的开始行数（默认填0，0开始,传过来是EXcel的行数值默认从1开始，这里已处理减1） EndRow：读取的结束行数（填-1为全部）
   * ExistTop:是否存在头部（如存在则读取数据时会把头部拼接到对应数据，若无则为当前列数）
   */
  private static List<List<Map<String, Object>>> HandleData(
      Workbook workbook, int StatrRow, int EndRow, boolean ExistTop, String SheetNum) {
    // 声明返回结果集result
    List<List<Map<String, Object>>> result = new ArrayList<>();
    // 解析sheet（sheet是Excel脚页）
    if (SheetNum == "-1") {
      Integer[] SheetNumArray = new Integer[workbook.getNumberOfSheets()];
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        SheetNumArray[i] = i;
      }
      result = ReadData(workbook, StatrRow, EndRow, ExistTop, SheetNumArray);
    } else {
      String[] split = SheetNum.split(",");
      Integer[] SheetNumArray = new Integer[split.length];
      for (int i = 0; i < split.length; i++) {
        SheetNumArray[i] = Integer.valueOf(split[i]);
      }
      result = ReadData(workbook, StatrRow, EndRow, ExistTop, SheetNumArray);
    }
    return result;
  }

  /** HandleData函数数据读取实际操作的抽离部分 */
  private static List<List<Map<String, Object>>> ReadData(
      Workbook workbook, int StatrRow, int EndRow, boolean ExistTop, Integer[] SheetNumArray) {
    // 声明返回结果集result
    List<List<Map<String, Object>>> result = new ArrayList<>();
    // 声明一个Excel头部函数
    ArrayList<String> top = new ArrayList<>();
    for (int sheetNum = 0; sheetNum < SheetNumArray.length; sheetNum++) {
      // 声明中间返回结果集result
      List<Map<String, Object>> middleResult = new ArrayList<>();
      Sheet sheet = workbook.getSheetAt(SheetNumArray[sheetNum]);
      // 校验sheet是否合法
      if (sheet == null) {
        continue;
      }
      // 如存在头部，处理头部数据
      if (ExistTop) {
        int firstRowNum = sheet.getFirstRowNum();
        Row firstRow = sheet.getRow(firstRowNum);
        if (null == firstRow) {
          logger.warn("解析Excel失败，在第一行没有读取到任何数据！");
        }
        for (int i = 0; i < firstRow.getLastCellNum(); i++) {
          top.add(convertCellValueToString(firstRow.getCell(i)));
        }
      }
      // 处理Excel数据内容
      int endRowNum;
      // 获取结束行数
      if (EndRow == -1) {
        endRowNum = sheet.getPhysicalNumberOfRows();
      } else {
        endRowNum =
            EndRow <= sheet.getPhysicalNumberOfRows() ? EndRow : sheet.getPhysicalNumberOfRows();
      }
      // 遍历行数
      for (int i = StatrRow - 1; i < endRowNum; i++) {
        Row row = sheet.getRow(i);
        if (null == row) {
          continue;
        }
        Map<String, Object> map = new HashMap<>();
        // 获取所有列数据
        for (int y = 0; y < row.getLastCellNum(); y++) {
          if (top.size() > 0) {
            if (top.size() >= y) {
              map.put(top.get(y), convertCellValueToString(row.getCell(y)));
            } else {
              map.put(String.valueOf(y + 1), convertCellValueToString(row.getCell(y)));
            }
          } else {
            map.put(String.valueOf(y + 1), convertCellValueToString(row.getCell(y)));
          }
        }
        middleResult.add(map);
      }
      result.add(middleResult);
    }
    return result;
  }

  /**
   * 方法一 根据行数和列数读取Excel fileName:Excel文件路径 StatrRow：读取的开始行数（默认填0） EndRow：读取的结束行数（填-1为全部）
   * ExistTop:是否存在头部（如存在则读取数据时会把头部拼接到对应数据，若无则为当前列数）
   * SheetNum:Excel工作表页数，以,分割，全选为-1(从0开始的整数，就是Excel下面那个sheet) 返回一个List<List<Map<String,Object>>>
   */
  public static List<List<Map<String, Object>>> ReadExcelByRC(
      String fileName, int StatrRow, int EndRow, boolean ExistTop, String SheetNum) {
    // 判断输入的开始值是否大于等0
    if (StatrRow < 0) {
      logger.warn("输入的开始行值小于0，请重新输入");
      return null;
    }
    // 判断输入的开始值是否少于等于结束值
    if (StatrRow > EndRow && EndRow != -1) {
      logger.warn("输入的开始行值比结束行值大，请重新输入正确的行数");
      return null;
    }
    if (SheetNum == null || SheetNum.isEmpty() || "".equals(SheetNum)) {
      logger.warn("请输入需要读取的Sheet页数，页数从0开始");
      return null;
    }
    // 声明返回的结果集
    List<List<Map<String, Object>>> result = new ArrayList<>();
    // 声明一个工作薄
    Workbook workbook = null;
    // 声明一个文件输入流
    FileInputStream inputStream = null;
    try {
      // 获取Excel后缀名，判断文件类型
      String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
      // 获取Excel文件
      File excelFile = new File(fileName);
      if (!excelFile.exists()) {
        logger.warn("指定的Excel文件不存在！");
        return null;
      }
      // 获取Excel工作簿
      inputStream = new FileInputStream(excelFile);
      workbook = getWorkbook(inputStream, fileType);
      // 处理Excel内容
      result = HandleData(workbook, StatrRow, EndRow, ExistTop, SheetNum);
    } catch (Exception e) {
      logger.warn("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
    } finally {
      try {
        if (null != workbook) {
          workbook.close();
        }
        if (null != inputStream) {
          inputStream.close();
        }
      } catch (Exception e) {
        logger.warn("关闭数据流出错！错误信息：" + e.getMessage());
        return null;
      }
    }
    return result;
  }

  /**
   * ==============================================================================================================================*
   */

  /**
   * 方法二 根据给定的实体类中赋值的注解值读取Excel fileName:Excel文件路径 StatrRow：读取的开始行数（默认填0） EndRow：读取的结束行数（填-1为全部）
   * Class<T>：传过来的实体类类型 SheetNum:Excel工作表页数，以,分割，全选为-1(从0开始的整数，就是Excel下面那个sheet)
   * 返回一个List<List<T>>:T为实体类
   */
  public static List<List<Object>> ReadExcelByPOJO(
      String fileName, int StatrRow, int EndRow, Class<?> t, String SheetNum)
      throws InvocationTargetException,
          IntrospectionException,
          InstantiationException,
          IllegalAccessException,
          NoSuchFieldException {
    // 判断输入的开始值是否少于等于结束值
    if (StatrRow > EndRow && EndRow != -1) {
      logger.warn("输入的开始行值比结束行值大，请重新输入正确的行数");
      return null;
    }
    // 声明返回的结果集
    List<List<Object>> result = new ArrayList<>();
    // 声明一个工作薄
    Workbook workbook = null;
    // 声明一个文件输入流
    FileInputStream inputStream = null;
    try {
      // 获取Excel后缀名，判断文件类型
      String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
      // 获取Excel文件
      File excelFile = new File(fileName);
      if (!excelFile.exists()) {
        logger.warn("指定的Excel文件不存在！");
        return null;
      }
      // 获取Excel工作簿
      inputStream = new FileInputStream(excelFile);
      workbook = getWorkbook(inputStream, fileType);
      // 处理Excel内容
      result = HandleDataPOJO(workbook, StatrRow, EndRow, t, SheetNum);
    } catch (Exception e) {
      logger.warn("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
    } finally {
      try {
        if (null != workbook) {
          workbook.close();
        }
        if (null != inputStream) {
          inputStream.close();
        }
      } catch (Exception e) {
        logger.warn("关闭数据流出错！错误信息：" + e.getMessage());
        return null;
      }
    }
    return result;
  }

  /**
   * 处理Excel内容转为List<T>输出 workbook：已连接的工作薄 StatrRow：读取的开始行数（默认填0，0开始,传过来是EXcel的行数值默认从1开始，这里已处理减1）
   * EndRow：读取的结束行数（填-1为全部） Class<T>：所映射的实体类
   */
  private static <t> List<List<Object>> HandleDataPOJO(
      Workbook workbook, int StatrRow, int EndRow, Class<?> t, String SheetNum)
      throws IllegalAccessException,
          IntrospectionException,
          InstantiationException,
          InvocationTargetException,
          NoSuchFieldException {
    // 声明返回结果集result
    List<List<Object>> result = new ArrayList<>();
    // 解析sheet（sheet是Excel脚页）
    if (SheetNum == "-1") {
      Integer[] SheetNumArray = new Integer[workbook.getNumberOfSheets()];
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        SheetNumArray[i] = i;
      }
      result = ReadDataPOJO(workbook, StatrRow, EndRow, t, SheetNumArray);
    } else {
      String[] split = SheetNum.split(",");
      Integer[] SheetNumArray = new Integer[split.length];
      for (int i = 0; i < split.length; i++) {
        SheetNumArray[i] = Integer.valueOf(split[i]);
      }
      result = ReadDataPOJO(workbook, StatrRow, EndRow, t, SheetNumArray);
    }
    return result;
  }

  /** HandleDataPOJO函数数据读取实际操作的抽离部分 */
  private static <t> List<List<Object>> ReadDataPOJO(
      Workbook workbook, int StatrRow, int EndRow, Class<?> t, Integer[] SheetNum)
      throws IntrospectionException,
          NoSuchFieldException,
          IllegalAccessException,
          InstantiationException,
          InvocationTargetException {
    // 声明返回的结果集
    List<List<Object>> result = new ArrayList<>();
    // 解析sheet（sheet是Excel脚页）
    for (int sheetNum = 0; sheetNum < SheetNum.length; sheetNum++) {
      // 声明返回的中间结果集
      List<Object> middleResult = new ArrayList<Object>();
      Sheet sheet = workbook.getSheetAt(SheetNum[sheetNum]);
      // 校验sheet是否合法
      if (sheet == null) {
        continue;
      }
      // 获取头部数据
      // 声明头部数据数列对象
      ArrayList<String> top = new ArrayList<>();
      // 获取Excel第一行数据
      int firstRowNum = sheet.getFirstRowNum();
      Row firstRow = sheet.getRow(firstRowNum);
      if (null == firstRow) {
        logger.warn("解析Excel失败，在第一行没有读取到任何数据！");
        return null;
      }
      for (int i = 0; i < firstRow.getLastCellNum(); i++) {
        top.add(convertCellValueToString(firstRow.getCell(i)));
      }
      // 获取实体类的成原变量
      Map<String, Object> POJOfields = getPOJOFieldAndValue(t);
      // 判断所需要的数据列
      Map<String, Object> exceltoPOJO = new HashMap<>();
      for (int i = 0; i < top.size(); i++) {
        if (POJOfields.get(top.get(i)) != null && !"".equals(POJOfields.get(top.get(i)))) {
          exceltoPOJO.put(String.valueOf(i), POJOfields.get(top.get(i)));
        }
      }
      /*处理Excel数据内容*/
      int endRowNum;
      // 获取结束行数
      if (EndRow == -1) {
        endRowNum = sheet.getPhysicalNumberOfRows();
      } else {
        endRowNum =
            EndRow <= sheet.getPhysicalNumberOfRows() ? EndRow : sheet.getPhysicalNumberOfRows();
      }
      List<Map<String, Object>> mapList = new ArrayList<>();
      // 遍历行数
      for (int i = StatrRow - 1; i < endRowNum; i++) {
        Row row = sheet.getRow(i);
        if (null == row) {
          continue;
        }
        // 获取需要的列数据
        t texcel = null;
        try {
          texcel = (t) t.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
        for (Map.Entry<String, Object> map : exceltoPOJO.entrySet()) {
          // 获取Excel对应列的数据
          String celldata = convertCellValueToString(row.getCell(Integer.parseInt(map.getKey())));
          // 获取实体类T中指定成员变量的对象
          PropertyDescriptor pd =
              new PropertyDescriptor((String) map.getValue(), texcel.getClass());
          // 获取成员变量的set方法
          Method method = pd.getWriteMethod();
          // 判断成员变量的类型
          Field field = texcel.getClass().getDeclaredField((String) map.getValue());
          String object = field.getGenericType().getTypeName();
          if (object.endsWith("String")) {
            if (celldata != null && !"".equals(celldata) && !celldata.isEmpty()) {
              // 执行set方法
              method.invoke(texcel, celldata);
            } else {
              /*什么都不用做，甚至这个else都可以不要，因为实体类初始化时会给对应的成员变量赋予空值 */
            }
          }
          if (object.endsWith("Double")) {
            System.out.println(celldata);
            if (celldata != null && !"".equals(celldata) && !celldata.isEmpty()) {
              Double middata = Double.valueOf(celldata);
              System.out.println("middata:" + middata);
              // 执行set方法
              method.invoke(texcel, middata);
            } else {
              /*什么都不用做，甚至这个else都可以不要，因为实体类初始化时会给对应的成员变量赋予空值 */
            }
          }
          if (object.endsWith("Float")) {
            if (celldata != null && !"".equals(celldata) && !celldata.isEmpty()) {
              Float middata = Float.valueOf(celldata);
              // 执行set方法
              method.invoke(texcel, middata);
            } else {
              /*什么都不用做，甚至这个else都可以不要，因为实体类初始化时会给对应的成员变量赋予空值 */
            }
          }
          if (object.endsWith("Integer")) {
            if (celldata != null && !"".equals(celldata) && !celldata.isEmpty()) {
              Integer middata = Integer.parseInt(celldata);
              // 执行set方法
              method.invoke(texcel, middata);
            } else {
              /*什么都不用做，甚至这个else都可以不要，因为实体类初始化时会给对应的成员变量赋予空值 */
            }
          }
        }
        middleResult.add(texcel);
      }
      result.add(middleResult);
    }
    return result;
  }

  /** 获取对应的实体类成员 */
  private static Map<String, Object> getPOJOFieldAndValue(Class T) {
    // 声明返回结果集
    Map<String, Object> result = new HashMap<>();
    // 获取属性名
    Field[] fields = T.getDeclaredFields();
    if (fields != null) {
      for (Field field : fields) {
        excelRescoure Rescoure = field.getAnnotation(excelRescoure.class);
        if (Rescoure.value() != null && !"".equals(Rescoure.value())) {
          result.put(Rescoure.value(), field.getName());
        }
      }
    } else {
      logger.info("实体类：" + T + "不存在成员变量");
      return null;
    }
    return result;
  }
}
