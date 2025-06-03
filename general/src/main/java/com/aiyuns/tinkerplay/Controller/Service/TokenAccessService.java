package com.aiyuns.tinkerplay.Controller.Service;

import com.aiyuns.tinkerplay.Entity.TokenAccess;

/**
 * @Author: aiYunS @Date: 2022-10-12 下午 04:46 @Description: 权限验证service层
 */
public interface TokenAccessService {

  // 查询用户权限状态
  int checkStatus(TokenAccess tokenAccess);
}
