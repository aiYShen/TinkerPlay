package com.aiyuns.tinkerplay.Other.Play;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * @Author: aiYunS @Date: 2023年7月18日, 0018 下午 2:12:35 @Description: 这是一个从本地多个路径下去读取.sql文件,
 * 分别执行sql语句到不同的数据库环境的小工具. 所以我讨厌sql.
 */
public class HateSql {

  static Map<String, String> paths;
  static Map<String, List<String>> excel = new HashMap<>();
  static String nowdate;
  static String path = "E:\\sql\\脚本更新记录表.xlsx";
  static XSSFWorkbook workbook;
  static XSSFSheet sheet;
  static FileOutputStream outputStream;
  static File file = new File(path);

  static {
    // 初始化当前日期
    nowdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    // 初始化要读取的文件路径
    paths.put("prd", "E:\\sql\\prd");
    paths.put("sit", "E:\\sql\\sit");
    paths.put("sit-prd", "E:\\sql\\sit-prd");
    paths.put("sit-uat", "E:\\sql\\sit-uat");
    paths.put("sit-uat-prd", "E:\\sql\\sit-uat-prd");
    paths.put("uat", "E:\\sql\\uat");
    paths.put("uat-prd", "E:\\sql\\uat-prd");
    // 初始化Map<String, List<String>> excel
    ArrayList arrayList = new ArrayList();
    arrayList.add("首行");
    excel.put("表头", arrayList);
    excel.put("prd", new ArrayList<>());
    excel.put("sit", new ArrayList<>());
    excel.put("sit-prd", new ArrayList<>());
    excel.put("sit-uat", new ArrayList<>());
    excel.put("sit-uat-prd", new ArrayList<>());
    excel.put("uat", new ArrayList<>());
    excel.put("uat-prd", new ArrayList<>());
    try {
      if (file.exists()) {
        workbook = new XSSFWorkbook(new FileInputStream(file));
        sheet = workbook.getSheet("更新记录");
        if (sheet.getRow(1) != null) {
          // 初始化加载已执行过的记录
          for (Row row : sheet) {
            if ("".equals(row.getCell(1).getStringCellValue())
                || row.getCell(1).getStringCellValue() == null) {
              continue;
            }
            if (!excel.keySet().contains(row.getCell(2).getStringCellValue())) {
              continue;
            }
            excel.get(row.getCell(2).getStringCellValue()).add(row.getCell(1).getStringCellValue());
          }
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    int threadNum = 0;
    LinkedHashMap<String, ArrayList<String>> sqlMap = null;
    for (String key : paths.keySet()) {
      sqlMap = new LinkedHashMap<>();
      String[] environments = key.split("-");
      String filePaths = paths.get(key);
      File file = new File(filePaths);
      // 过滤出.sql后缀的文件
      FilenameFilter filenameFilter =
          new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
              return name.endsWith(".sql");
            }
          };
      File[] files = file.listFiles(filenameFilter);
      if (files == null || files.length == 0) {
        continue;
      }
      // 去除已执行过的文件
      ArrayList<File> ff = new ArrayList<>(Arrays.asList(files));
      ff =
          (ArrayList<File>)
              ff.stream()
                  .filter(f -> !excel.get(key).contains(f.getName()))
                  .collect(Collectors.toList());
      if (ff == null || ff.size() == 0) {
        continue;
      }
      readSql(ff, sqlMap);
      try {
        if (environments.length == 1) {
          DB db = new DB(environments[0], sqlMap);
          ++threadNum;
          db.setName(threadNum + "-" + environments[0] + "环境");
          db.start();
          db.join();
        } else {
          for (String environment : environments) {
            DB db = new DB(environment, sqlMap);
            ++threadNum;
            db.setName(threadNum + "-" + environment + "环境");
            db.start();
            db.join();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    // 执行过的文件写入excel记录表
    if (DB.writeResault != null && DB.writeResault.size() > 0) {
      writeExcel(DB.writeResault);
    }
    long endTime = System.currentTimeMillis();
    System.out.println("==============================程序执行耗时============================");
    System.out.println("程序执行耗时: " + (endTime - startTime) / 1000 + "秒");
    System.out.println("====================================================================");
    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<输出有问题的文件>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    for (String key : DB.errorInfo.keySet()) {
      System.out.println(key + "-->" + DB.errorInfo.get(key));
    }
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
  }

  public static LinkedHashMap<String, ArrayList<String>> readSql(
      ArrayList<File> files, LinkedHashMap<String, ArrayList<String>> sqlMap) {
    BufferedReader bufferedReader = null;
    try {
      for (File file : files) {
        InputStream inputStream = new FileInputStream(file);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line;
        boolean isAdd = false;
        StringBuilder sql = new StringBuilder();
        ArrayList<String> sqls = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
          if (!isBlankLine(line)) {
            if (!isComment(line)) {
              if (!line.endsWith(";")) {
                sql.append(line);
                sql.append(" ");
                isAdd = true;
              } else {
                if (sql != null && !"".equals(sql.toString()) && sql.toString().length() != 0) {
                  sqls.add(sql.append(line).toString());
                  sql.setLength(0);
                } else {
                  sqls.add(line);
                }
                isAdd = false;
              }
            }
          }
        }
        if (isAdd) {
          sqls.add(sql.append(";").toString());
        }
        if (sqls.size() > 0) {
          sqlMap.put(file.toString(), sqls);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sqlMap;
  }

  public static String fileEncodingDetector(InputStream inputStream) {
    try {
      byte[] buffer = new byte[4096];
      UniversalDetector detector = new UniversalDetector(null);

      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) > 0 && !detector.isDone()) {
        detector.handleData(buffer, 0, bytesRead);
      }
      detector.dataEnd();
      String charsetName = detector.getDetectedCharset();
      detector.reset();
      if (charsetName != null) {
        return charsetName;
      } else {
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static boolean isComment(String line) {
    Pattern pattern = Pattern.compile("^\\s*--");
    Matcher matcher = pattern.matcher(line);
    return matcher.find();
  }

  public static boolean isBlankLine(String line) {
    Pattern pattern = Pattern.compile("^\\s*$");
    Matcher matcher = pattern.matcher(line);
    return matcher.matches();
  }

  private static void writeExcel(HashSet<String> hashSet) {
    try {
      if (!file.exists()) {
        file.createNewFile();
        HateSql.workbook = new XSSFWorkbook();
        HateSql.sheet = HateSql.workbook.createSheet("更新记录");
        XSSFRow row0 = HateSql.sheet.createRow(0);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor((short) 28);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        style.setFont(font);
        row0.setRowStyle(style);
        String[] infos = {"表名", "文件名称", "待刷环境", "修改时间", "提交人", "是否已刷", "备注"};
        for (int i = 0; i < infos.length; i++) {
          row0.createCell(i).setCellValue(infos[i]);
        }
        ArrayList arrayList = new ArrayList<>();
        arrayList.add("首行");
        excel.put("表头", arrayList);
      }
      for (String filePath : hashSet) {
        int rowIndex = 0;
        for (String key : HateSql.excel.keySet()) {
          rowIndex += HateSql.excel.get(key).size();
        }
        XSSFRow row = HateSql.sheet.createRow(rowIndex);
        if (filePath.contains("(") && filePath.contains(")")) {
          row.createCell(0)
              .setCellValue(
                  filePath.substring(filePath.lastIndexOf("-") + 1, filePath.lastIndexOf("(")));
        } else {
          row.createCell(0)
              .setCellValue(
                  filePath.substring(filePath.lastIndexOf("-") + 1, filePath.lastIndexOf(".")));
        }
        row.createCell(1).setCellValue(filePath.substring(filePath.lastIndexOf("\\") + 1));
        row.createCell(2)
            .setCellValue(
                filePath.substring(
                    filePath.indexOf("\\", filePath.indexOf("\\") + 1) + 1,
                    filePath.lastIndexOf("\\")));
        row.createCell(3).setCellValue(HateSql.nowdate);
        row.createCell(4).setCellValue("aiYunS");
        row.createCell(5).setCellValue("是");
        HateSql.excel
            .get(
                filePath.substring(
                    filePath.indexOf("\\", filePath.indexOf("\\") + 1) + 1,
                    filePath.lastIndexOf("\\")))
            .add(filePath.substring(filePath.lastIndexOf("\\") + 1));
      }
      outputStream = new FileOutputStream(file);
      HateSql.workbook.write(outputStream);
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean SqlValidator(String sql) {
    try {
      // 将SQL语句解析成语法树
      net.sf.jsqlparser.statement.Statement statement = CCJSqlParserUtil.parse(sql);
      // 如果解析成功，则认为SQL合法
      return true;
    } catch (Exception e) {
      // 解析失败，SQL不合法
      return false;
    }
  }
}
