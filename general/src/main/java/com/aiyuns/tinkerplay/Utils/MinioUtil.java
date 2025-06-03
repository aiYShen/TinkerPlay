package com.aiyuns.tinkerplay.Utils;

import com.aiyuns.tinkerplay.Config.MinioClientConfig;
import com.aiyuns.tinkerplay.Entity.ResultEntity;
import com.alibaba.cloud.commons.lang.StringUtils;
import io.minio.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: aiYunS @Date: 2023年6月7日, 0007 下午 4:55:10 @Description: minio桶工具类
 */
@Slf4j
@Component
public class MinioUtil {
  /**
   * Minio文件上传
   *
   * @param file 文件实体
   * @param fileName 修饰过的文件名 非源文件名
   * @param bucketName 所存文件夹（桶名）
   * @return
   */
  public ResultEntity<Map<String, Object>> minioUpload(
      MultipartFile file, String fileName, String bucketName) {
    ResultEntity<Map<String, Object>> resultEntity = new ResultEntity();
    try {
      MinioClient minioClient = MinioClientConfig.getMinioClient();
      // fileName为空，说明要使用源文件名上传
      if (fileName == null) {
        fileName = file.getOriginalFilename();
        fileName = fileName.replaceAll(" ", "_");
      }
      InputStream inputStream = file.getInputStream();
      PutObjectArgs objectArgs =
          PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                  inputStream, file.getSize(), -1)
              .contentType(file.getContentType())
              .build();
      // 文件名称相同会覆盖
      minioClient.putObject(objectArgs);
      return resultEntity;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 检查存储桶是否存在
   *
   * @param bucketName 存储桶名称
   * @return
   */
  public boolean bucketExists(String bucketName) {
    boolean flag = false;
    try {
      flag = MinioClientConfig.bucketExists(bucketName);
      if (flag) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }

  /**
   * 获取文件流
   *
   * @param fileName 文件名
   * @param bucketName 桶名（文件夹）
   * @return
   */
  public InputStream getFileInputStream(String fileName, String bucketName) {
    try {
      MinioClient minioClient = MinioClientConfig.getMinioClient();
      return minioClient.getObject(
          GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return null;
  }

  /**
   * @param bucketName:
   * @author
   * @description: 创建桶
   * @date 2022/8/16 14:36
   */
  public void createBucketName(String bucketName) {
    try {
      if (StringUtils.isBlank(bucketName)) {
        return;
      }
      MinioClient minioClient = MinioClientConfig.getMinioClient();
      boolean isExist = MinioClientConfig.bucketExists(bucketName);
      if (isExist) {
        log.info("Bucket {} already exists.", bucketName);
      } else {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
  }

  /**
   * 下载文件
   *
   * @param originalName 文件路径
   */
  public static InputStream downloadFile(
      String bucketName, String originalName, String openStyle, HttpServletResponse response) {
    try {
      MinioClient minioClient = MinioClientConfig.getMinioClient();
      GetObjectArgs objectArgs =
          GetObjectArgs.builder().bucket(bucketName).object(originalName).build();
      InputStream file = minioClient.getObject(objectArgs);
      String filename = new String(originalName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
      if (StringUtils.isNotBlank(originalName)) {
        filename = originalName.substring(originalName.lastIndexOf("/") + 1, originalName.length());
      }
      response.setHeader(
          "Content-Disposition",
          openStyle + ";filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
      ServletOutputStream servletOutputStream = response.getOutputStream();
      int len;
      byte[] buffer = new byte[1024];
      while ((len = file.read(buffer)) > 0) {
        servletOutputStream.write(buffer, 0, len);
      }
      servletOutputStream.flush();
      file.close();
      servletOutputStream.close();
      return file;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * @param bucketName:
   * @description: 删除桶
   * @date 2022/8/16 14:36
   */
  public void deleteBucketName(String bucketName) {
    try {
      if (StringUtils.isBlank(bucketName)) {
        return;
      }
      MinioClient minioClient = MinioClientConfig.getMinioClient();
      boolean isExist = MinioClientConfig.bucketExists(bucketName);
      if (isExist) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
  }

  /**
   * @param bucketName:
   * @description: 删除桶下面所有文件
   * @date 2022/8/16 14:36
   */
  public void deleteBucketFile(String bucketName) {
    try {
      if (StringUtils.isBlank(bucketName)) {
        return;
      }
      MinioClient minioClient = MinioClientConfig.getMinioClient();
      boolean isExist =
          minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (isExist) {
        minioClient.deleteBucketEncryption(
            DeleteBucketEncryptionArgs.builder().bucket(bucketName).build());
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
  }

  /**
   * 根据文件路径得到预览文件绝对地址
   *
   * @param bucketName
   * @param fileName
   * @return
   */
  public String getPreviewFileUrl(String bucketName, String fileName) {
    try {
      MinioClient minioClient = MinioClientConfig.getMinioClient();
      return minioClient.getPresignedObjectUrl(
          GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).build());
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
}
