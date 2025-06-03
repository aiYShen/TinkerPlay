package com.aiyuns.tinkerplay.Controller.Service;

import com.aiyuns.tinkerplay.Entity.*;
import com.aiyuns.tinkerplay.Utils.RowConvertColUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @Author: aiYunS @Date: 2021/6/30 上午 11:30 @Description: Service层
 */
public interface UserService {

  /**
   * @Author: aiYunS @Description: 查 @Date: 2021/6/30 上午 11:31 @Param:
   *
   * @return: java.util.List<com.aiyuns.tinkerplay.entity.user>
   */
  List<User> findAll();

  Future<List<User>> findAll2();

  // 增
  R insertOne(User user);

  // 增: 测试RabbitMQ延迟删除数据,先新增后,延迟删除
  R insertOne2(User user);

  // 增: 测试新增获取ID
  R insertOne3(User user);

  // 删
  void deleteByName(QueryRequestVo queryRequestVo);

  // 删: 测试RabbitMQ延迟删除数据
  void deleteByName2(QueryRequestVo queryRequestVo);

  // 改
  void updateOne(QueryRequestVo queryRequestVo);

  // 根据id查
  R findById(QueryRequestVo queryRequestVo);

  // 根据name查
  User findByName(QueryRequestVo queryRequestVo);

  // 根据时间区间查询
  List<User> findByTime(QueryRequestVo queryRequestVo);

  // 行转列
  RowConvertColUtil.ConvertData RowConvertCol(QueryRequestVo queryRequestVo);

  // 批量插入
  void insertAll(List<User> userList);

  // 获取IP或MAC地址
  Map getIPorMACaddress(HttpServletRequest request) throws UnknownHostException, Exception;

  // 消除转义
  ResultMsg removeESC();

  // 批量查询
  // List findByprojbase(List<Projbase> projbase);
}
