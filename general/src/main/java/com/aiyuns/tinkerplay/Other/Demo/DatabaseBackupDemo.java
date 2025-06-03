package com.aiyuns.tinkerplay.Other.Demo;

import com.aiyuns.tinkerplay.Utils.FileCompressionUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipOutputStream;

/**
 * @Author: aiYunS @Date: 2023年6月26日, 0026 上午 9:40:09 @Description: 数据库备份demo
 */
public class DatabaseBackupDemo {
  public static void main(String[] args) {
    String host = "localhost";
    String username = "root";
    String password = "19930218";
    String databaseName = "tinkerplay";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String backupFilePath = "E:\\aiyuns\\DatabaseBackup\\" + sdf.format(new Date());
    String filename = "\\backup.sql";
    File file = new File(backupFilePath);
    if (!file.isDirectory()) {
      file.mkdirs();
    }
    file = new File(backupFilePath + filename);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // Linux
    // String command = String.format("mysqldump -u %s -p%s %s > %s", username, password,
    // databaseName, backupFilePath);
    // Windows
    String command =
        "mysqldump -u"
            + username
            + " -p"
            + password
            + " "
            + databaseName
            + " -r "
            + backupFilePath
            + filename;

    try {
      // Linux
      // ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
      // Windwos
      ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
      Process process = processBuilder.start();
      int exitCode = process.waitFor();

      if (exitCode == 0) {
        System.out.println("数据库备份成功！");
      } else {
        System.out.println("数据库备份失败！");
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    // 压缩数据库备份
    String zipFilePath = "D:\\Users\\lenovo\\Desktop\\" + sdf.format(new Date()) + ".zip";
    try {
      FileOutputStream fos = new FileOutputStream(zipFilePath);
      ZipOutputStream zos = new ZipOutputStream(fos);

      File sourceFolder = new File(backupFilePath);
      FileCompressionUtil.compressFolder(sourceFolder, sourceFolder.getName(), zos);

      zos.close();
      fos.close();

      System.out.println("文件压缩成功！");
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 压缩成功后删除源文件
    // file.delete();
    // System.out.println("文件删除成功！");

    // 压缩成功后删除源文件及其路径
    File file1 = new File("E:\\aiyuns\\DatabaseBackup");
    deleteDirectory(file1);
    System.out.println("目录及其文件删除成功！");
  }

  public static void deleteDirectory(File directory) {
    if (directory.isDirectory()) {
      File[] files = directory.listFiles();
      if (files != null) {
        for (File file : files) {
          deleteDirectory(file);
        }
      }
    }
    directory.delete();
  }
}
