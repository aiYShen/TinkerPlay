package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao;

import com.aiyuns.tinkerplay.Entity.Projbase;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import java.util.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: aiYunS @Date: 2021/6/30 上午 11:28 @Description: ProjBase DAO层
 */
@Mapper
@CacheConfig(cacheNames = "ProjBaseDao")
public interface ProjBaseDao {

  // 查询projbase表总记录条数
  @DS("slave_1")
  long CountProjbase();

  Long selectCarArchivesList_COUNT();

  // 循环查询,每次查询固定的条数
  @DS("slave_1")
  List<Projbase> readProjbase(Map map);

  // 写入数据
  @DS("slave_1")
  void writeProjbaseException(List list);

  @DS("slave_1")
  Page<Projbase> selectProjbase1();

  @DS("slave_1")
  Page<Projbase> selectProjbase2();
}
