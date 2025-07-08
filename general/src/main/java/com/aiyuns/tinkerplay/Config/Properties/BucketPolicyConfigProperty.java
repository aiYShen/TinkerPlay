package com.aiyuns.tinkerplay.Config.Properties;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Minio Bucket访问策略配置 */
@Data
@EqualsAndHashCode
@Builder
public class BucketPolicyConfigProperty {

  private String Version;
  private List<Statement> Statement;

  @Data
  @EqualsAndHashCode
  @Builder
  public static class Statement {
    private String Effect;
    private String Principal;
    private String Action;
    private String Resource;
  }
}
