package com.aiyuns.tinkerplay.Other.Demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.xwpf.usermodel.*;

/**
 * @Author: aiYunS @Date: 2023年6月29日, 0029 下午 4:54:12 @Description: word读取demo
 */
public class WordContentExtractorDemo {
  public static void main(String[] args) {
    try {
      // 加载Word文档
      FileInputStream fis = new FileInputStream("path/to/your/document.docx");
      XWPFDocument document = new XWPFDocument(fis);

      // 遍历文档段落
      List<XWPFParagraph> paragraphs = document.getParagraphs();
      for (XWPFParagraph paragraph : paragraphs) {
        // 提取段落文本
        String text = paragraph.getText();

        // 根据自定义条件判断是否提取该段落
        if (text.contains("your condition")) {
          System.out.println(text);
        }
      }

      // 遍历文档表格
      List<XWPFTable> tables = document.getTables();
      for (XWPFTable table : tables) {
        // 遍历表格行
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
          // 遍历行单元格
          List<XWPFTableCell> cells = row.getTableCells();
          for (XWPFTableCell cell : cells) {
            // 提取单元格文本
            String text = cell.getText();

            // 根据自定义条件判断是否提取该单元格
            if (text.contains("your condition")) {
              System.out.println(text);
            }
          }
        }
      }

      // 关闭文档和输入流
      document.close();
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
