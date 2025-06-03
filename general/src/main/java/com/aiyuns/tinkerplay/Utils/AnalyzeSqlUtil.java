package com.aiyuns.tinkerplay.Utils;

import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;

/**
 * @Author: aiYunS @Date: 2023年11月16日, 0016 下午 3:56:32 @Description: 简单分析SQL执行内容
 */
public class AnalyzeSqlUtil {
  public static String analyze(String sql) {
    try {
      StringBuilder stringBuilder = new StringBuilder();
      Statement statement = CCJSqlParserUtil.parse(sql);
      if (statement instanceof Alter) {
        // 处理 ALTER TABLE 语句
        Alter alter = (Alter) statement;
        String tableName = alter.getTable().getName();
        if (sql.contains("modify") || sql.contains("MODIFY")) {
          stringBuilder.append("修改: ");
        } else {
          stringBuilder.append("新增: ");
        }
        stringBuilder.append(tableName);
        stringBuilder.append(" 表结构 ");
        List<AlterExpression> columnNames = alter.getAlterExpressions();
        if (columnNames != null && columnNames.size() > 0) {
          for (int i = 0; i < columnNames.size(); i++) {
            stringBuilder.append(columnNames.get(i).getColDataTypeList().get(0).getColumnName());
            if (sql.contains("COMMENT") || sql.contains("comment")) {
              stringBuilder.append("(");
              List<String> columnSpecs =
                  columnNames.get(i).getColDataTypeList().get(0).getColumnSpecs();
              stringBuilder.append(columnSpecs.get(indexOfIgnoreCase(columnSpecs, "COMMENT") + 1));
              stringBuilder.append(")");
            }
            if (i != columnNames.size() - 1) {
              stringBuilder.append("、");
            } else {
              stringBuilder.append("，");
            }
          }
        }
        stringBuilder.append(columnNames.size());
        stringBuilder.append("个字段");
      } else if (statement instanceof CreateTable) {
        // 处理其他类型的 SQL 语句
        stringBuilder.append("新建: ");
        stringBuilder.append(((CreateTable) statement).getTable().getName());
        List<String> tableOptionsStrings = ((CreateTable) statement).getTableOptionsStrings();
        if (tableOptionsStrings.contains("COMMENT") || tableOptionsStrings.contains("comment")) {
          stringBuilder.append("(");
          stringBuilder.append(
              tableOptionsStrings.get(indexOfIgnoreCase(tableOptionsStrings, "comment") + 2));
          stringBuilder.append(")");
        }
        stringBuilder.append(" 表");
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
        stringBuilder.append("个字段");
      } else if (statement instanceof Insert) {
        stringBuilder.append("向表: ");
        stringBuilder.append(((Insert) statement).getTable().getName());
        stringBuilder.append(" 插入数据");
      } else if (statement instanceof Delete) {
        stringBuilder.append("删除表: ");
        stringBuilder.append(((Delete) statement).getTable().getName());
        stringBuilder.append(" 条件: ");
        stringBuilder.append(((Delete) statement).getWhere().toString());
        stringBuilder.append(" 的数据");
      } else if (statement instanceof Drop) {
        stringBuilder.append("删除表结构: ");
        stringBuilder.append(((Drop) statement).getName());
      } else if (statement instanceof CreateIndex) {
        stringBuilder.append(((CreateIndex) statement).getTable().getName());
        stringBuilder.append(" 表, 新建: ");
        stringBuilder.append(((CreateIndex) statement).getIndex().getType());
        stringBuilder.append(" 类型索引");
        stringBuilder.append("(");
        stringBuilder.append(((CreateIndex) statement).getIndex().getName());
        stringBuilder.append(": ");
        List<String> indesCols = ((CreateIndex) statement).getIndex().getColumnsNames();
        for (int i = 0; i < indesCols.size(); i++) {
          stringBuilder.append(indesCols.get(i));
          stringBuilder.append(" 字段");
          if (indesCols.size() - 1 == i) {
            stringBuilder.append(".");
          } else {
            stringBuilder.append(", ");
          }
        }
        stringBuilder.append(") ");
      }
      return stringBuilder.toString();
    } catch (JSQLParserException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static int indexOfIgnoreCase(List<String> list, String target) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equalsIgnoreCase(target)) {
        return i;
      }
    }
    // 如果未找到匹配项
    return -1;
  }
}
