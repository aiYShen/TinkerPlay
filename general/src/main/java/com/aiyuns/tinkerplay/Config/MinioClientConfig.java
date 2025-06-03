package com.aiyuns.tinkerplay.Config;

import com.aiyuns.tinkerplay.Config.Properties.StorageProperty;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2023年6月7日, 0007 下午 4:57:41 @Description: Minio配置类
 */
@Slf4j
@Component
@Configuration
public class MinioClientConfig {
  @Autowired private StorageProperty storageProperty;

  private static MinioClient minioClient;

  /**
   * @description: 获取minioClient
   * @return io.minio.MinioClient
   */
  public static MinioClient getMinioClient() {
    return minioClient;
  }

  /**
   * 判断 bucket是否存在
   *
   * @param bucketName: 桶名
   * @return: boolean
   */
  @SneakyThrows(Exception.class)
  public static boolean bucketExists(String bucketName) {
    return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
  }

  /**
   * 获取全部bucket
   *
   * @param :
   * @return: java.util.List<io.minio.messages.Bucket>
   */
  @SneakyThrows(Exception.class)
  public static List<Bucket> getAllBuckets() {
    return minioClient.listBuckets();
  }

  // 初始化minio配置
  @PostConstruct
  public void init() {
    try {
      minioClient =
          MinioClient.builder()
              .endpoint(storageProperty.getEndpoint())
              .credentials(storageProperty.getAccessKey(), storageProperty.getSecretKey())
              .build();
    } catch (Exception e) {
      e.printStackTrace();
      log.error("初始化minio配置异常: 【{}】", e.fillInStackTrace());
    }
  }
}
