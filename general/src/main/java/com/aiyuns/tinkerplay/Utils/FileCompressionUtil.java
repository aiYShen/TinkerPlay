package com.aiyuns.tinkerplay.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: aiYunS @Date: 2023年6月26日, 0026 下午 3:41:46 @Description: 文件压缩工具类
 */
public class FileCompressionUtil {

  public static void main(String[] args) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String sourceFolderPath = "E:\\aiyuns\\DatabaseBackup\\20230626";
    String zipFilePath = "D:\\Users\\lenovo\\Desktop\\" + sdf.format(new Date()) + ".zip";
    // String zipFilePath = sourceFolderPath + "\\" + sdf.format(new Date()) + ".zip";

    try {
      FileOutputStream fos = new FileOutputStream(zipFilePath);
      ZipOutputStream zos = new ZipOutputStream(fos);

      File sourceFolder = new File(sourceFolderPath);
      FileCompressionUtil.compressFolder(sourceFolder, sourceFolder.getName(), zos);

      zos.close();
      fos.close();

      System.out.println("文件压缩成功！");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void compressFolder(File folder, String parentFolder, ZipOutputStream zos)
      throws IOException {
    for (File file : folder.listFiles()) {
      if (file.isDirectory()) {
        compressFolder(file, parentFolder + "/" + file.getName(), zos);
      } else {
        compressFile(file, parentFolder, zos);
      }
    }
  }

  public static void compressFile(File file, String parentFolder, ZipOutputStream zos)
      throws IOException {
    byte[] buffer = new byte[1024];
    FileInputStream fis = new FileInputStream(file);
    zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

    int length;
    while ((length = fis.read(buffer)) > 0) {
      zos.write(buffer, 0, length);
    }

    zos.closeEntry();
    fis.close();
  }
}
