package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao;

import com.aiyuns.tinkerplay.Entity.SysJobPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: aiYunS @Date: 2022-10-10 下午 02:36 @Description: 定时任务DAO层
 */
@Mapper
public interface ISysJobDao {

  // 新增定时任务
  boolean addSysJob(SysJobPO sysJobPO);

  // 获取正常状态的定时任务
  List<SysJobPO> getSysJobListByStatus(int jobStatus);

  // 修改定时任务
  boolean editSysJob(SysJobPO sysJobPO);

  // 删除定时任务
  boolean deleteSysJob(int jobId);

  // 获取所有的定时任务
  List<SysJobPO> findAllSysJob();
}
