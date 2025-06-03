package com.aiyuns.tinkerplay.Config.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2023年6月7日, 0007 下午 4:59:49 @Description: Minio属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class StorageProperty {
  private String endpoint;
  private String accessKey;
  private String secretKey;
}
