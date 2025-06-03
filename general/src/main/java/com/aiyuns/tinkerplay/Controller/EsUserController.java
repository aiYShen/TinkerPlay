package com.aiyuns.tinkerplay.Controller;

import com.aiyuns.tinkerplay.Common.CommonPage;
import com.aiyuns.tinkerplay.Common.CommonResult;
import com.aiyuns.tinkerplay.Controller.Service.EsUserService;
import com.aiyuns.tinkerplay.Entity.EsUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: aiYunS @Date: 2023年3月16日, 0016 下午 1:59:46 @Description: ES的Controller
 */
@Controller
@Tag(name = "EsUserController", description = "Elasticsearch模块")
@RequestMapping("/esUser")
public class EsUserController {
  @Resource EsUserService esUserService;

  @Operation(summary = "根据名字计数")
  @GetMapping("/countByname/{name}")
  @ResponseBody
  public long countByname(@PathVariable("name") String name) {
    return esUserService.countByname(name);
  }

  @Operation(summary = "根据名字删除")
  @GetMapping("/deleteByname/{name}")
  @ResponseBody
  public long deleteByname(@PathVariable("name") String name) {
    return esUserService.deleteByname(name);
  }

  @Operation(summary = "根据名字移除")
  @GetMapping("/removeByname/{name}")
  @ResponseBody
  List<EsUser> removeByname(@PathVariable("name") String name) {
    return esUserService.removeByname(name);
  }

  @Operation(summary = "保存全部到ES")
  @RequestMapping(value = "/esUserSaveAll", method = RequestMethod.POST)
  @ResponseBody
  String userSaveAll() {
    int result = esUserService.saveAllUsers();
    return "成功插入: " + result + "条数据!";
  }

  @Operation(summary = "查询ES")
  @RequestMapping(value = "/findByName", method = RequestMethod.POST)
  @ResponseBody
  List findByName(String name) {
    List<EsUser> userList = esUserService.findByName(name);
    return userList;
  }

  @Operation(summary = "根据ID查询ES")
  @ResponseBody
  @GetMapping("/searchById/{id}")
  public Optional<EsUser> searchById(@PathVariable("id") Long id) {
    Optional<EsUser> esUser = esUserService.searchById(id);
    return esUser;
  }

  @Operation(summary = "保存到ES")
  @ResponseBody
  @PostMapping("/saveEsUser")
  public Long saveEsUser(@RequestBody EsUser esUser) {
    Long id = esUserService.saveEsUser(esUser);
    return id;
  }

  @Operation(summary = "查询所有")
  @GetMapping("/searchAll")
  @ResponseBody
  public SearchHits<EsUser> searchAll() {
    return esUserService.searchAll();
  }

  @Operation(summary = "导入所有数据库中人员信息到ES")
  @RequestMapping(value = "/importAll", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<Integer> importAllList() {
    int count = esUserService.importAll();
    return CommonResult.success(count);
  }

  @Operation(summary = "根据id删除人员")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult<Object> delete(@PathVariable Long id) {
    esUserService.delete(id);
    return CommonResult.success(null);
  }

  @Operation(summary = "根据id批量删除人员信息")
  @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<Object> delete(@RequestParam("ids") List<Long> ids) {
    esUserService.delete(ids);
    return CommonResult.success(null);
  }

  @Operation(summary = "根据id创建人员")
  @RequestMapping(value = "/create/{id}", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<EsUser> create(@PathVariable Long id) {
    EsUser esProduct = esUserService.create(id);
    if (esProduct != null) {
      return CommonResult.success(esProduct);
    } else {
      return CommonResult.failed();
    }
  }

  @Operation(summary = "根据性别简单搜索")
  @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult<CommonPage<EsUser>> search(
      @RequestParam(required = false) String sex,
      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
    Page<EsUser> esUserPage = esUserService.searchBySex(sex, pageNum, pageSize);
    return CommonResult.success(CommonPage.restPage(esUserPage));
  }

  @Operation(summary = "根据姓名或关键字简单搜索")
  @RequestMapping(value = "/search/searchNameOrKeyword", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult<CommonPage<EsUser>> searchNameOrKeyword(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
    Page<EsUser> esUserPage = esUserService.searchByNameOrKeywords(keyword, pageNum, pageSize);
    return CommonResult.success(CommonPage.restPage(esUserPage));
  }

  @Operation(summary = "根据姓名或证件号或关键字简单搜索")
  @RequestMapping(value = "/search/searchByNameOrIDCARDOrKeywords", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult<CommonPage<EsUser>> searchByNameOrCARDOrKeywords(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
    Page<EsUser> esUserPage =
        esUserService.searchByNameOrCARDOrKeywords(keyword, pageNum, pageSize);
    return CommonResult.success(CommonPage.restPage(esUserPage));
  }

  @Operation(summary = "关键字搜索")
  @RequestMapping(value = "/search/searchKeyword", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult<CommonPage<EsUser>> searchKeyword(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
    Page<EsUser> esUserPage = esUserService.searchByKeyword(keyword, pageNum, pageSize);
    return CommonResult.success(CommonPage.restPage(esUserPage));
  }
}
