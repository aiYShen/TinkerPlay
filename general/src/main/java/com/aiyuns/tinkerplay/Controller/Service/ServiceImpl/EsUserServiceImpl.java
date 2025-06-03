package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl;

import com.aiyuns.tinkerplay.Controller.Service.EsUserService;
import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.Repository.EsUserRepository;
import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.UserDao;
import com.aiyuns.tinkerplay.Entity.EsUser;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @Author: aiYunS @Date: 2023年9月13日, 0013 下午 2:21:15 @Description: es的service实现层
 */
@Service
public class EsUserServiceImpl implements EsUserService {

  private final EsUserRepository esUserRepository;
  private final ElasticsearchRestTemplate elasticsearchTemplate;
  @Resource private UserDao userDao;

  @Autowired
  public EsUserServiceImpl(
      EsUserRepository esUserRepository, ElasticsearchRestTemplate elasticsearchTemplate) {
    this.esUserRepository = esUserRepository;
    this.elasticsearchTemplate = elasticsearchTemplate;
  }

  @Override
  public long countByname(String name) {
    return esUserRepository.countByname(name);
  }

  @Override
  public long deleteByname(String name) {
    return esUserRepository.deleteByname(name);
  }

  @Override
  public List<EsUser> removeByname(String name) {
    return esUserRepository.removeByname(name);
  }

  @Override
  public int saveAllUsers() {
    int result = 0;
    List<EsUser> esUserList = userDao.findAll3();
    Iterable<EsUser> esUserIterable = elasticsearchTemplate.save(esUserList);
    Iterator<EsUser> iterator = esUserIterable.iterator();
    while (iterator.hasNext()) {
      result++;
      iterator.next();
    }
    return result;
  }

  @Override
  public List findByName(String name) {
    return esUserRepository.findByname(name);
  }

  @Override
  public Optional<EsUser> searchById(Long id) {
    return esUserRepository.findById(id);
  }

  @Override
  public Long saveEsUser(EsUser esUser) {
    return esUserRepository.save(esUser).getId();
  }

  @Override
  public SearchHits<EsUser> searchAll() {
    MatchAllQueryBuilder matchAllQueryBuilder = new MatchAllQueryBuilder();
    NativeSearchQuery query =
        new NativeSearchQueryBuilder().withQuery(matchAllQueryBuilder).build();
    return elasticsearchTemplate.search(query, EsUser.class);
  }

  @Override
  public int importAll() {
    List<EsUser> esProductList = userDao.findAll3();
    Iterable<EsUser> esProductIterable = esUserRepository.saveAll(esProductList);
    Iterator<EsUser> iterator = esProductIterable.iterator();
    int result = 0;
    while (iterator.hasNext()) {
      result++;
      iterator.next();
    }
    return result;
  }

  @Override
  public void delete(Long id) {
    esUserRepository.deleteById(id);
  }

  @Override
  public EsUser create(Long id) {
    EsUser result = null;
    List<EsUser> esUserList = userDao.findById2(id);
    if (esUserList.size() > 0) {
      EsUser esProduct = esUserList.get(0);
      result = (EsUser) esUserRepository.save(esProduct);
    }
    return result;
  }

  @Override
  public void delete(List<Long> ids) {
    if (!CollectionUtils.isEmpty(ids)) {
      List<EsUser> esProductList = new ArrayList<>();
      for (Long id : ids) {
        EsUser esProduct = new EsUser();
        esProduct.setId(id);
        esProductList.add(esProduct);
      }
      esUserRepository.deleteAll(esProductList);
    }
  }

  @Override
  public Page<EsUser> searchBySex(String sex, Integer pageNum, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNum, pageSize);
    return esUserRepository.findBySex(sex, pageable);
  }

  @Override
  public Page<EsUser> searchByNameOrKeywords(String keyword, Integer pageNum, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNum, pageSize);
    return esUserRepository.findByNameOrKeywords(keyword, keyword, pageable);
  }

  @Override
  public Page<EsUser> searchByNameOrCARDOrKeywords(
      String keyword, Integer pageNum, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNum, pageSize);
    return esUserRepository.findByNameOrCARDOrKeywords(keyword, keyword, keyword, pageable);
  }

  @Override
  public Page<EsUser> searchByKeyword(String keyword, Integer pageNum, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNum, pageSize);
    return esUserRepository.findByKeywords(
        keyword, keyword, keyword, keyword, keyword, keyword, keyword, pageable);
  }
}
