package com.aiyuns.tinkerplay.Other.Play;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/*
   这是一个从本地多个文件夹读取.sql文件,
   分别执行sql语句到不同数据库的小工具.
   所以我讨厌SQL!
*/
public class HateSqlDemo {

  protected static ConcurrentHashMap<String, Boolean> svnPaths = new ConcurrentHashMap<>();
  protected static Map<String, String> analyzeSqlResult = new HashMap<>();
  private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static Map<String, Boolean> warnSql = new HashMap<>();
  private static Map<String, String> paths;
  private static Map<String, List<String>> excel = new HashMap<>();
  private static XSSFWorkbook workbook;
  private static XSSFSheet sheet;
  private static File file = new File("E:\\sql\\xxxxx.xlsx");
  private static volatile boolean threadComletedOne = false;
  private static volatile boolean threadComletedTwo = false;
  private static volatile boolean isSVNUpdate = false;
  private static final ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
  private static final SVNClientManager svnClientManager =
      SVNClientManager.newInstance((DefaultSVNOptions) options, "xxx", "xxx");

  static {
    svnPaths.put("xxxxxx", true);
    svnPaths.put("xxxxxx", true);
    paths = new TreeMap<>();
    paths.put("xxx", "E:\\sql\\xxx");
    paths.put("xxx", "E:\\sql\\xxx");
    paths.put("xxx-xxx", "E:\\sql\\xxx-xxx");
    paths.put("xxx-xxx", "E:\\sql\\xxx-xxx");
    paths.put("xxx-xxx-xxx", "E:\\sql\\xxx-xxx-xxx");
    paths.put("xxx", "E:\\sql\\xxx");
    paths.put("xxx-xxx", "E:\\sql\\xxx-xxx");
    paths.put("xxxx", "E:\\sql\\xxxx");
    ArrayList arrayList = new ArrayList();
    arrayList.add("首行");
    HateSql.excel.put("表头", arrayList);
    HateSql.excel.put("xxx", new ArrayList<>());
    HateSql.excel.put("xxx", new ArrayList<>());
    HateSql.excel.put("xxx-xxx", new ArrayList<>());
    HateSql.excel.put("xxx-xxx", new ArrayList<>());
    HateSql.excel.put("xxx-xxx-xxx", new ArrayList<>());
    HateSql.excel.put("xxx", new ArrayList<>());
    HateSql.excel.put("xxx-xxx", new ArrayList<>());
    HateSql.excel.put("xxxx", new ArrayList<>());
    try {
      if (file.exists()) {
        FileInputStream inputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheet("更新记录");
        if (sheet.getRow(1) != null) {
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
        inputStream.close();
      }
      if (true) {
        Thread virtualThread =
            Thread.startVirtualThread(
                () -> {
                  while (true) {
                    if (threadComletedOne && threadComletedTwo) {
                      System.out.println();
                      System.out.println(" 开始执行SVN提交步骤: ");
                      isSVNUpdate = false;
                      threadComletedOne = false;
                      threadComletedTwo = false;
                      commitSVN();
                      System.out.println(" SVN提交完成! ");
                    }
                  }
                });
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    int threadNum = 0;
    LinkedHashMap<String, ArrayList<String>> sqlMap = null;
    for (String key : paths.keySet()) {
      sqlMap = new LinkedHashMap<>();
      String[] environments = key.split("-");
      String filePaths = paths.get(key);
      File file = new File(filePaths);
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
      ArrayList<File> ff = new ArrayList<>(Arrays.asList(files));
      ff =
          (ArrayList<File>)
              ff.stream()
                  .filter(f -> !HateSql.excel.get(key).contains(f.getName()))
                  .collect(Collectors.toList());
      if (ff.isEmpty()) {
        continue;
      }
      readSql(ff, sqlMap);
      analyzeSql(sqlMap);
      if (HateSqlDemo.warnSql.containsValue(true) || !DB2.errorInfo.isEmpty()) {
        return;
      }
      try {
        for (int i = 0; i < environments.length; i++) {
          DB2 db = new DB2(environments[i], sqlMap);
          Thread thread = Thread.ofVirtual().start(db);
          ++threadNum;
          thread.setName(threadNum + "-" + environments[i] + "环境");
          thread.start();
          thread.join();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (!DB2.writeResault.isEmpty() && !HateSqlDemo.analyzeSqlResult.isEmpty()) {
      long time = System.currentTimeMillis();
      while (true) {
        if (System.currentTimeMillis() - time < 60000) {
          if (isSVNUpdate) {
            break;
          }
        } else if (System.currentTimeMillis() - time > 60000) {
          DB2.errorInfo.put("SVN更新超时!!", "SQL执行成功! 但未写入Excel! 请手动填补!");
          return;
        }
      }
      Thread virtualThreadOne =
          Thread.startVirtualThread(
              () -> {
                writeExcelTwo();
              });
      Thread virtualThreadTwo =
          Thread.startVirtualThread(
              () -> {
                writeExcelOne();
              });
    }
    long endTime = System.currentTimeMillis();
    System.out.println();
    System.out.println("============================程序执行耗时===========================");
    System.out.println("程序执行耗时: " + (endTime - startTime) / 1000 + "秒");
    System.out.println("=================================================================");
    if (DB2.errorInfo != null && DB2.errorInfo.size() > 0) {
      System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<打印有问题的文件>>>>>>>>>>>>>>>>>>>>>>>>>>");
      for (String key : DB2.errorInfo.keySet()) {
        System.out.println(key + " --> " + DB2.errorInfo.get(key));
      }
      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
  }

  private static synchronized void doCleanup() {
    for (String windowsPath : svnPaths.keySet()) {
      File file = new File(windowsPath);
      if (file.exists()) {
        try {
          svnClientManager.getWCClient().doCleanup(file);
          System.out.println("执行Cleanup完成!");
        } catch (SVNException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static synchronized void updateSVN() {
    try {
      DAVRepositoryFactory.setup();
      SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
      updateClient.setIgnoreExternals(false);
      for (String windowsPath : svnPaths.keySet()) {
        if (svnPaths.get(windowsPath)) {
          long startTime = System.currentTimeMillis();
          File updateFile = new File(windowsPath);
          long versionNum =
              updateClient.doUpdate(updateFile, SVNRevision.HEAD, SVNDepth.INFINITY, false, false);
          System.out.println(
              simpleDateFormat.format(new Date())
                  + ", "
                  + windowsPath
                  + ", SVN 更新完成! 当前版本号为: "
                  + versionNum
                  + ", 耗时: "
                  + (System.currentTimeMillis() - startTime)
                  + " ms");
        }
      }
      isSVNUpdate = true;
    } catch (SVNException e) {
      e.printStackTrace();
      doCleanup();
    }
  }

  private static LinkedHashMap<String, ArrayList<String>> readSql(
      ArrayList<File> files, LinkedHashMap<String, ArrayList<String>> sqlMap) {
    FileInputStream stream = null;
    InputStreamReader reader = null;
    BufferedReader bufferedReader = null;
    try {
      for (File file : files) {
        stream = new FileInputStream(file);
        reader = new InputStreamReader(stream, "UTF-8");
        bufferedReader = new BufferedReader(reader);
        String line;
        boolean isAdd = false;
        StringBuilder sql = new StringBuilder();
        ArrayList<String> sqls = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
          if (!isCommentOrBlankLine(line)) {
            if (line.endsWith(" ") && line.contains(";")) {
              line = line.substring(0, line.lastIndexOf(";") + 1);
            }
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
        if (reader != null) {
          reader.close();
        }
        if (stream != null) {
          stream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sqlMap;
  }

  private static void analyzeSql(LinkedHashMap<String, ArrayList<String>> sqlMap) {
    for (String key : sqlMap.keySet()) {
      StringBuilder stringBuilder = new StringBuilder();
      for (String sql : sqlMap.get(key)) {
        try {
          net.sf.jsqlparser.statement.Statement statement = CCJSqlParserUtil.parse(sql);
          if (statement instanceof Alter) {
            Alter alter = (Alter) statement;
            String tableName = alter.getTable().getName();
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            sb.append(tableName);
            sb.append("表");
            if (sql.contains("modify") || sql.contains("MODIFY")) {
              sb.append("修改字段: ");
            } else if (sql.contains("DORP") || sql.contains("drop")) {
              if (sql.contains("drop index") || sql.contains("DROP INDEX")) {
                sb.append("删除索引: ");
              } else {
                sb.append("删除字段: ");
              }
            } else if (sql.contains(" index ") || sql.contains(" INDEX ")) {
              sb.append("新增索引: ");
            } else {
              sb.append("新增字段: ");
            }
            String last = "";
            if (stringBuilder.length() > 0) {
              last =
                  stringBuilder
                      .toString()
                      .substring(0, stringBuilder.toString().lastIndexOf(":"))
                      .substring(
                          stringBuilder
                                  .toString()
                                  .substring(0, stringBuilder.toString().lastIndexOf(":"))
                                  .lastIndexOf(" ")
                              + 1);
            }
            if (StringUtils.isBlank(stringBuilder.toString()) || !sb.toString().contains(last)) {
              stringBuilder.append(sb);
            }
            List<AlterExpression> columnNames = alter.getAlterExpressions();
            if (columnNames != null && columnNames.size() > 0) {
              for (int i = 0; i < columnNames.size(); i++) {
                if (columnNames.get(i).getColDataTypeList() != null) {
                  stringBuilder.append(
                      columnNames.get(i).getColDataTypeList().get(0).getColumnName());
                } else if ("drop".equals(columnNames.get(i).getOperation().name().toLowerCase())) {
                  if (columnNames.get(i).getColumnName() != null) {
                    stringBuilder.append(columnNames.get(i).getColumnName());
                  } else if (StringUtils.isNotBlank(columnNames.get(i).getIndex().getName())) {
                    stringBuilder.append(columnNames.get(i).getIndex().getName());
                  } else {
                    stringBuilder.append("空!");
                  }
                } else if (columnNames.get(i).getIndex() != null) {
                  stringBuilder.append(columnNames.get(i).getIndex().getName());
                }
                if (sql.contains("comment") || sql.contains("COMMENT")) {
                  stringBuilder.append("(");
                  List<String> columnSpecs =
                      columnNames.get(i).getColDataTypeList().get(0).getColumnSpecs();
                  stringBuilder.append(
                      columnSpecs.get(indexOfIgnoreCase(columnSpecs, "COMMENT") + 1));
                  stringBuilder.append(")");
                }
                if (i != columnNames.size() - 1) {
                  stringBuilder.append(", ");
                } else {
                  stringBuilder.append(". ");
                }
              }
            }
          } else if (statement instanceof CreateTable) {
            stringBuilder.append("新建: ");
            stringBuilder.append(((CreateTable) statement).getTable().getName());
            List<String> tableOptionsStrings = ((CreateTable) statement).getTableOptionsStrings();
            if (tableOptionsStrings.contains("comment")
                || tableOptionsStrings.contains("COMMENT")) {
              stringBuilder.append("(");
              stringBuilder.append(
                  tableOptionsStrings.get(indexOfIgnoreCase(tableOptionsStrings, "comment") + 2));
              stringBuilder.append(")");
            }
            stringBuilder.append(" 表. ");
          } else if (statement instanceof Update) {
            stringBuilder.append("更新: ");
            stringBuilder.append(((Update) statement).getTable().getName());
            stringBuilder.append(" 表的 ");
            List<UpdateSet> updateSet = ((Update) statement).getUpdateSets();
            for (int i = 0; i < updateSet.size(); i++) {
              stringBuilder.append(updateSet.get(i).getColumns().get(0));
              stringBuilder.append(", ");
            }
            stringBuilder.append(updateSet.size());
            stringBuilder.append("个字段. ");
          } else if (statement instanceof Insert) {
            stringBuilder.append("向表: ");
            stringBuilder.append(((Insert) statement).getTable().getName());
            stringBuilder.append(" 插入数据. ");
          } else if (sql.startsWith("REPLACE") || sql.startsWith("replace")) {
            stringBuilder.append("使用 REPLACE INTO 插入数据. ");
          } else if (statement instanceof Delete) {
            stringBuilder.append("删除表: ");
            stringBuilder.append(((Delete) statement).getTable().getName());
            stringBuilder.append(", 条件: ");
            if (((Delete) statement).getWhere() != null
                && ((Delete) statement).getWhere().toString().length() > 0) {
              stringBuilder.append(((Delete) statement).getWhere().toString());
              stringBuilder.append(" 的数据. ");
            } else {
              stringBuilder.append("无. ");
              DB2.errorInfo.put(sql, "DELETE语句无WHERE条件! 危险!!!");
            }
            if (!HateSqlDemo.warnSql.containsKey(sql)) {
              HateSqlDemo.warnSql.put(sql, true);
            }
          } else if (statement instanceof Drop) {
            stringBuilder.append("删除表结构: ");
            stringBuilder.append(((Drop) statement).getName());
            stringBuilder.append(" . ");
            if (!HateSqlDemo.warnSql.containsKey(sql)) {
              HateSqlDemo.warnSql.put(sql, true);
            }
          } else if (statement instanceof Select) {
            stringBuilder.append("查询: ");
            stringBuilder.append(sql.substring(sql.lastIndexOf(" ")));
            stringBuilder.append(" 表的数据. ");
          } else if (statement instanceof CreateIndex) {
            stringBuilder.append(((CreateIndex) statement).getTable().getName());
            stringBuilder.append(" 表, 新建: ");
            stringBuilder.append(((CreateIndex) statement).getIndex().getType());
            stringBuilder.append(" 类型索引");
            stringBuilder.append("(");
            stringBuilder.append(((CreateIndex) statement).getIndex().getName());
            stringBuilder.append(": ");
            List<String> index = ((CreateIndex) statement).getIndex().getColumnsNames();
            for (int i = 0; i < index.size(); i++) {
              stringBuilder.append(index.get(i));
              stringBuilder.append(" 字段");
              if (i == index.size() - 1) {
                stringBuilder.append(".");
              } else {
                stringBuilder.append(", ");
              }
            }
            stringBuilder.append(") ");
          }
        } catch (JSQLParserException e) {
          if ((sql.startsWith("CREATE INDEX") || sql.startsWith("create index"))
              && e.toString()
                  .contains(
                      "net.sf.jsqlparser.parser.ParseException: Encountered unexpected token:"
                          + " \"USING\" \"USING\"")) {
            stringBuilder.append("新建索引");
          } else {
            DB2.errorInfo.put(key + " --> " + sql, " 当前sql语句格式化错误,请检查语句!!!");
          }
          e.printStackTrace();
        }
      }
      analyzeSqlResult.put(key, stringBuilder.toString());
    }
  }

  private static synchronized void writeExcelOne() {
    try {
      long startTime = System.currentTimeMillis();
      if (!file.exists()) {
        file.createNewFile();
        HateSql.workbook = new XSSFWorkbook();
        HateSql.sheet = HateSql.workbook.createSheet("更新记录");
        XSSFRow row0 = HateSql.sheet.createRow(0);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor((short) 28);
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
        ArrayList arrayList = new ArrayList();
        arrayList.add("首行");
        HateSql.excel.put("表头", arrayList);
      }
      for (String filePath : DB2.writeResault) {
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
        row.createCell(3).setCellValue(HateSqlDemo.simpleDateFormat.format(new Date()));
        row.createCell(4).setCellValue("baiyx");
        row.createCell(5).setCellValue("是");
        HateSql.excel
            .get(
                (filePath.substring(
                    filePath.indexOf("\\", filePath.indexOf("\\") + 1) + 1,
                    filePath.lastIndexOf("\\"))))
            .add(filePath.substring(filePath.lastIndexOf("\\") + 1));
      }
      FileOutputStream outputStream = new FileOutputStream(file);
      HateSql.workbook.write(outputStream);
      outputStream.flush();
      outputStream.close();
      new Thread(
              () -> {
                try {
                  FileInputStream inputStream = new FileInputStream(file);
                  HateSql.workbook = new XSSFWorkbook(inputStream);
                  HateSql.sheet = HateSql.workbook.getSheet("更新记录");
                  inputStream.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
          .start();
      DB2.writeResault.clear();
      threadComletedOne = true;
      System.out.println(
          "写入: 脚本更新记录 Excel表 完成! 耗时: " + (System.currentTimeMillis() - startTime) + " ms");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static synchronized void writeExcelTwo() {
    try {
      long startTime = System.currentTimeMillis();
      File path = new File("E:\\同步修改\\04系统xx\\05数据库xx\\xxxxx.xlsm");
      FileInputStream inputStream = null;
      Workbook workbook = null;
      Sheet sheet = null;
      if (path.exists()) {
        outLoop:
        for (String sqlpath : analyzeSqlResult.keySet()) {
          if (inputStream != null) {
            inputStream.close();
          }
          inputStream = new FileInputStream("E:\\同步修改\\04系统xx\\05数据库xx\\xxxxx.xlsm");
          workbook = new XSSFWorkbook(inputStream);
          sheet = workbook.getSheet("修订记录");
          CellStyle cellStyle = workbook.createCellStyle();
          cellStyle.setAlignment(HorizontalAlignment.CENTER);
          cellStyle.setWrapText(true);
          String tableName =
              sqlpath
                  .substring(sqlpath.lastIndexOf("-") + 1, sqlpath.lastIndexOf("."))
                  .toLowerCase()
                  .replace(" ", "");
          String message = analyzeSqlResult.get(sqlpath).toLowerCase();
          String cel8 =
              sqlpath
                  .substring(0, sqlpath.lastIndexOf("-"))
                  .substring(sqlpath.substring(0, sqlpath.lastIndexOf("-")).lastIndexOf("-") + 1);
          Map<String, Integer> map = new HashMap<String, Integer>();
          if (sqlpath.contains("sit")) {
            map.put("sit", 2);
          }
          if (sqlpath.contains("uat")) {
            map.put("uat", 4);
          }
          if (sqlpath.contains("prd")) {
            map.put("prd", 6);
          }
          boolean flag = false;
          for (Row row : sheet) {
            String cel0 =
                row.getCell(0) == null
                    ? ""
                    : row.getCell(0).getStringCellValue().toLowerCase().replace(" ", "");
            String cel1 =
                row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue().toLowerCase();
            String cel2 = row.getCell(2) == null ? "" : row.getCell(2).getStringCellValue();
            String cel4 = row.getCell(4) == null ? "" : row.getCell(4).getStringCellValue();
            String cel6 = row.getCell(6) == null ? "" : row.getCell(6).getStringCellValue();
            if (StringUtils.isNotBlank(cel2)
                && StringUtils.isNotBlank(cel4)
                && StringUtils.isNotBlank(cel6)) {
              continue;
            }
            if (tableName.equals(cel0)
                && (cel1.contains(message) || message.contains(cel1))
                && (StringUtils.isBlank(cel2)
                    || StringUtils.isBlank(cel4)
                    || StringUtils.isBlank(cel6))) {
              boolean outLoopFlag = false;
              if (cel1.equals(message)) {
                outLoopFlag = true;
              }
              boolean writeFlag = false;
              for (String s : map.keySet()) {
                if (row.getCell(map.get(s)) == null && row.getCell(map.get(s) + 1) == null) {
                  row.createCell(map.get(s)).setCellValue(s);
                  row.createCell(map.get(s) + 1).setCellValue(simpleDateFormat.format(new Date()));
                  writeFlag = true;
                }
              }
              if (!cel1.equals(message) && message.length() > cel1.length()) {
                row.getCell(1).setCellValue(message);
              }
              if (row.getCell(8) == null) {
                row.createCell(8).setCellValue(cel8);
              }
              if (writeFlag) {
                writeWorkbook(workbook);
                flag = true;
                if (outLoopFlag) {
                  continue outLoop;
                }
              }
            }
          }
          if (flag) {
            continue outLoop;
          }
          Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
          newRow.createCell(0).setCellValue(tableName);
          Cell cell1 = newRow.createCell(1);
          cell1.setCellValue(message);
          cell1.setCellStyle(cellStyle);
          for (String s : map.keySet()) {
            newRow.createCell(map.get(s)).setCellValue(s);
            newRow.createCell(map.get(s) + 1).setCellValue(simpleDateFormat.format(new Date()));
          }
          newRow.createCell(8).setCellValue(cel8);
          writeWorkbook(workbook);
        }
      }
      inputStream.close();
      HateSqlDemo.analyzeSqlResult.clear();
      threadComletedTwo = true;
      System.out.println(
          "写入: 零售客户信息系统_开发测试环境 Excel表 完成! 耗时: " + (System.currentTimeMillis() - startTime) + " ms");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static synchronized void commitSVN() {
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    try {
      for (String path : svnPaths.keySet()) {
        executor.execute(new CommitSVNTask(path));
      }
      executor.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
      doCleanup();
    }
  }

  private static boolean isCommentOrBlankLine(String line) {
    Pattern pattern = Pattern.compile("^\\s*--");
    Pattern pattern1 = Pattern.compile("^\\s*$");
    Matcher matcher = pattern.matcher(line);
    Matcher matcher1 = pattern1.matcher(line);
    return StringUtils.isBlank(line) || matcher.find() || matcher1.matches();
  }

  private static synchronized void writeWorkbook(Workbook workbook) {
    try {
      FileOutputStream outputStream = new FileOutputStream("E:\\同步修改\\04系统xx\\05数据库xx\\xxxxx.xlsm");
      workbook.write(outputStream);
      outputStream.flush();
      outputStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static int indexOfIgnoreCase(List<String> list, String target) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equalsIgnoreCase(target)) {
        return i;
      }
    }
    return -1;
  }

  static class CommitSVNTask implements Runnable {
    private String path;

    public CommitSVNTask(String path) {
      this.path = path;
    }

    @Override
    public void run() {
      try {
        long startTime = System.currentTimeMillis();
        SVNRepositoryFactoryImpl.setup();
        File commitFile = new File(path);
        SVNStatus status = svnClientManager.getStatusClient().doStatus(commitFile, true);
        if (status == null || status.getContentsStatus() == SVNStatusType.STATUS_UNVERSIONED) {
          svnClientManager
              .getWCClient()
              .doAdd(commitFile, false, false, false, SVNDepth.INFINITY, false, false);
        }
        svnClientManager
            .getCommitClient()
            .doCommit(
                new File[] {commitFile}, true, path, null, null, true, false, SVNDepth.INFINITY);
        System.out.println(
            simpleDateFormat.format(new Date())
                + ", "
                + path
                + ", SVN 提交完成! 耗时: "
                + (System.currentTimeMillis() - startTime)
                + " ms");
        if (!svnPaths.get(path)) {
          svnPaths.remove(path);
        }
      } catch (SVNException e) {
        e.printStackTrace();
        doCleanup();
      }
    }
  }
}

// 数据库
class DB2 implements Runnable {

  public static LinkedHashMap<String, String> errorInfo = new LinkedHashMap<>();
  protected static HashSet<String> writeResault = new HashSet<>();
  private static final String driverClass = "com.mysql.cj.jdbc.Driver";
  private static String[] environments = {
    "xxx=jdbc:mysql://xxx.xxx.xx.xxx:xxxxx/xxxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false",
    "xxx=jdbc:mysql://xxx.xxx.xx.xx:xxxxx/xxxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false",
    "xxx=jdbc:mysql://xxx.xxx.xx.xxx:xxxxx/xxxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false",
    "xxxx=jdbc:mysql://xxx.xxx.xx.xxx:xxxxx/xxxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false"
  };
  private static final String username = "xxxx";
  private static final String password = "xxxx";
  private Connection connection;
  private Statement statement;
  private LinkedHashMap<String, ArrayList<String>> sqlMap;

  public DB2(String environment, LinkedHashMap<String, ArrayList<String>> sqlMap) {
    try {
      Class.forName(driverClass);
      for (String developUrl : environments) {
        if (developUrl.startsWith(environment)) {
          switch (environment) {
            case "xxx":
              connection =
                  DriverManager.getConnection(
                      developUrl.replace(environment + "=", ""), "xxxx", "xxxxxx");
              break;
            default:
              connection =
                  DriverManager.getConnection(
                      developUrl.replace(environment + "=", ""), username, password);
          }
        }
      }
      connection.setAutoCommit(false);
      statement = connection.createStatement();
      this.sqlMap = sqlMap;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    for (String key : sqlMap.keySet()) {
      ArrayList<String> arrsql = sqlMap.get(key);
      boolean isSuccess = true;
      for (String sql : arrsql) {
        try {
          statement.execute(sql);
        } catch (Exception e) {
          if (e.toString().contains("Duplicate column name")) {
            errorInfo.put("可忽略错误: " + key, sql + " ==> SQL语句已执行过!");
            e.printStackTrace();
            continue;
          }
          isSuccess = false;
          errorInfo.put("不可忽略错误: " + key, sql + " ==> SQL语句存在错误!");
          rollback();
          e.printStackTrace();
          HateSqlDemo.analyzeSqlResult.remove(key);
          break;
        }
      }
      if (isSuccess) {
        try {
          connection.commit();
          writeResault.add(key);
          HateSqlDemo.svnPaths.put(key, false);
          System.out.println(
              Thread.currentThread().getName()
                  + " --> 子线程 <"
                  + key
                  + "> 文件执行成功! 耗时: "
                  + (System.currentTimeMillis() - startTime)
                  + " ms");
        } catch (Exception e) {
          rollback();
          e.printStackTrace();
        }
      } else {
        System.out.println(Thread.currentThread().getName() + " --> 子线程 <" + key + "> 文件执行失败!");
      }
    }
    this.close();
    System.out.println();
  }

  private void rollback() {
    try {
      if (connection != null) {
        connection.rollback();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void close() {
    try {
      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
