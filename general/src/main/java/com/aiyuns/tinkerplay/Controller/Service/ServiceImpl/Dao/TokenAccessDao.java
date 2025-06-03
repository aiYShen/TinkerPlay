package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao;

import com.aiyuns.tinkerplay.Entity.TokenAccess;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: aiYunS @Date: 2022-10-12 下午 04:39 @Description: 查询权限DAO层
 */
@Mapper
public interface TokenAccessDao {

  // 查询用户权限状态
  Integer checkStatus(TokenAccess tokenAccess);
}
