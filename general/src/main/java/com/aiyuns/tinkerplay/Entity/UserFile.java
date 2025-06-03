package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Author: aiYunS @Date: 2023年6月7日, 0007 上午 9:08:11 @Description: 文件下载实体类
 */
@Schema(name = "用户文件信息实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true) // 为生成的设置方法返回当前对象，以支持链式调用。
public class UserFile {

  @Schema(description = "文件ID")
  private Integer id;

  @Schema(description = "文件名称")
  private String fileName;

  @Schema(description = "文件后缀类型")
  private String ext;

  @Schema(description = "文件路径")
  private String path;

  @Schema(description = "文件大小")
  private long size;

  @Schema(description = "文件类型")
  private String type;

  @Schema(description = "下载次数")
  private Integer downloadCounts;

  @Schema(description = "上传时间")
  private Date uploadTime;

  @Schema(description = "用户id")
  private Integer userId;

  @Schema(description = "删除标识")
  private String deleted;
}
