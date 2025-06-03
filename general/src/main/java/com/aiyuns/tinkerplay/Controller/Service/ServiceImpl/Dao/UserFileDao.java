package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao;

import com.aiyuns.tinkerplay.Entity.UserFile;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: aiYunS @Date: 2023年6月7日, 0007 上午 9:13:21 @Description: 文件下载Dao
 */
@Mapper
public interface UserFileDao {

  /***
   * @Author: aiYunS
   * @Description: 根据用户ID查询file表文件信息
   * @Date: 2023年6月7日, 0007 上午 9:21:34
   * @Param:
   * @param id
   * @param begin
   * @param offset
   * @return: java.util.List<com.aiyuns.tinkerplay.Entity.UserFile>
   */
  List<UserFile> queryByUserId(Integer id, String deleted, Integer begin, int offset);

  /***
   * @Author: aiYunS
   * @Description: 查询文件总数
   * @Date: 2023年6月7日, 0007 上午 9:22:25
   * @Param:
   * @param id
   * @return: int
   */
  int queryFileCount(Integer id, String deleted);

  /***
   * @Author: aiYunS
   * @Description: 文件上传,写入数据库
   * @Date: 2023年6月7日, 0007 上午 10:01:08
   * @Param:
   * @param userFiles
   * @return: void
   */
  void save(List<UserFile> userFiles);

  /***
   * @Author: aiYunS
   * @Description: 文件下载
   * @Date: 2023年6月7日, 0007 上午 11:04:01
   * @Param:
   * @param id
   * @return: com.aiyuns.tinkerplay.Entity.UserFile
   */

  UserFile queryByUserFileId(Integer id);

  /***
   * @Author: aiYunS
   * @Description: 更新文件下载次数
   * @Date: 2023年6月7日, 0007 上午 11:04:14
   * @Param:
   * @param userFile
   * @return: void
   */
  void update(UserFile userFile);

  /***
   * @Author: aiYunS
   * @Description: 删除文件
   * @Date: 2023年6月8日, 0008 下午 3:45:38
   * @Param:
   * @param objectName
   * @return: void
   */
  void deleteFile(String objectName);

  /***
   * @Author: aiYunS
   * @Description: 逻辑删除文件
   * @Date: 2023年6月8日, 0008 下午 3:45:38
   * @Param:
   * @param objectName
   * @return: void
   */
  void falseDeleteFile(String objectName);

  /***
   * @Author: aiYunS
   * @Description: 根据文件名查询
   * @Date: 2023年6月12日, 0012 上午 9:43:36
   * @Param:
   * @param objectName
   * @return: com.aiyuns.tinkerplay.Entity.UserFile
   */
  UserFile queryByFileName(String objectName);
}
