package com.aiyuns.tinkerplay.Config.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2023年6月7日, 0007 下午 4:59:49 @Description: Minio属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = MinioProperty.PREFIX)
public class MinioProperty {
  public static final String PREFIX = "minio";

  private String endpoint;
  private String bucketName;
  private String accessKey;
  private String secretKey;
}
