package com.aiyuns.tinkerplay.Controller.Service;

import com.aiyuns.tinkerplay.Entity.EsUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHits;

/**
 * @Author: aiYunS @Date: 2023年9月13日, 0013 上午 11:19:58 @Description: EsUserService
 */
public interface EsUserService {

  long countByname(String name);

  long deleteByname(String name);

  List<EsUser> removeByname(String name);

  // 批量保存到es
  int saveAllUsers();

  // 按照姓名查找es
  List findByName(String lastName);

  Optional<EsUser> searchById(Long id);

  Long saveEsUser(EsUser esUser);

  SearchHits<EsUser> searchAll();

  /** 从数据库中导入所有人员信息到ES */
  int importAll();

  /** 根据id删除人员 */
  void delete(Long id);

  /** 根据id创建人员 */
  EsUser create(Long id);

  /** 批量删除人员 */
  void delete(List<Long> ids);

  /** 根据性别搜索 */
  Page<EsUser> searchBySex(String sex, Integer pageNum, Integer pageSize);

  /** 根据姓名或关键字搜索 */
  Page<EsUser> searchByNameOrKeywords(String keyword, Integer pageNum, Integer pageSize);

  /** 根据姓名或证件号或关键字搜索 */
  Page<EsUser> searchByNameOrCARDOrKeywords(String keyword, Integer pageNum, Integer pageSize);

  /** 根据关键字搜索 */
  Page<EsUser> searchByKeyword(String keywords, Integer pageNum, Integer pageSize);
}
