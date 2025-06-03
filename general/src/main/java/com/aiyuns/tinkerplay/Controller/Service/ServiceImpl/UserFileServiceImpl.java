package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl;

import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.UserFileDao;
import com.aiyuns.tinkerplay.Controller.Service.UserFileService;
import com.aiyuns.tinkerplay.Entity.UserFile;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: aiYunS @Date: 2023年6月7日, 0007 上午 9:27:18 @Description: 文件下载Service层实现层
 */
@Service
public class UserFileServiceImpl implements UserFileService {

  @Autowired private UserFileDao userFileDao;

  /**
   * 根据用户id获得文件列表
   *
   * @param id
   * @param page
   * @param limit
   * @return
   */
  @Override
  public List<UserFile> queryByUserId(Integer id, String deleted, Integer page, Integer limit) {
    // page表示第几页，limit表示每页显示多少行数据
    // 该计算方法获得开始的位置
    int begin = (page - 1) * limit;
    int offset = limit;
    return userFileDao.queryByUserId(id, deleted, begin, limit);
  }

  /**
   * 根据用户id获得文件数
   *
   * @param id
   * @return
   */
  @Override
  public int queryFileCounts(Integer id, String deleted) {
    return userFileDao.queryFileCount(id, deleted);
  }

  // 文件上传,信息保存到数据库
  @Override
  public void save(List<UserFile> userFiles) {
    for (UserFile userfile : userFiles) {
      userfile.setDownloadCounts(0).setUploadTime(new Date());
    }
    userFileDao.save(userFiles);
  }

  // 下载文件
  @Override
  public UserFile queryByUserFileId(Integer id) {
    return userFileDao.queryByUserFileId(id);
  }

  // 更新文件下载次数
  @Override
  public void update(UserFile userFile) {
    userFileDao.update(userFile);
  }

  // 删除文件
  @Override
  public void deleteFile(String objectName) {
    userFileDao.deleteFile(objectName);
  }

  @Override
  public void falseDeleteFile(String objectName) {
    userFileDao.falseDeleteFile(objectName);
  }

  // 根据文件名查询
  @Override
  public UserFile queryByFileName(String objectName) {
    return userFileDao.queryByFileName(objectName);
  }
}
