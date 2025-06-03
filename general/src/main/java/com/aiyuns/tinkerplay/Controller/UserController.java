package com.aiyuns.tinkerplay.Controller;

import com.aiyuns.tinkerplay.Algorithm.RecursiveAlgorithm;
import com.aiyuns.tinkerplay.Controller.Service.UserService;
import com.aiyuns.tinkerplay.CustomAnnotations.*;
import com.aiyuns.tinkerplay.Entity.QueryRequestVo;
import com.aiyuns.tinkerplay.Entity.R;
import com.aiyuns.tinkerplay.Entity.ResultMsg;
import com.aiyuns.tinkerplay.Entity.User;
import com.aiyuns.tinkerplay.Utils.ExcelUtil;
import com.aiyuns.tinkerplay.Utils.RowConvertColUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: aiYunS @Date: 2021/6/30 上午 11:35 @Description: 控制器-入口
 */
@RestController
@Component("TestTimedTask")
@RequestMapping(value = "userController", produces = "application/json;charset=UTF-8")
@EnableScheduling // 1.开启定时任务
@EnableAsync // 2.开启多线程
@Tag(name = "UserController", description = "常规功能模块")
public class UserController {

  @Autowired UserService UserService;

  /**
   * @Author: aiYunS @Description: 测试Aop层面统一打印自定义日志格式 测试RedisCacheConfig缓存管理器 测试@WebLog自定义日志打印的注解
   * 测试自定义定时任务功能 测试Springboot框架自带的定时任务注解 @EnableScheduling @Scheduled 测试@Async多线程异步调用 @Date:
   * 2021/6/30 上午 11:43 @Param:
   *
   * @return: java.util.List<com.aiyuns.tinkerplay.entity.user>
   */
  // @Scheduled(fixedDelay = 30000)  //间隔1秒
  // @Async //开启异步这里会导致controller层返回为null;
  @Operation(summary = "查询所有")
  @WebLog(description = "查询所有")
  @RequestMapping(value = "findAll", method = RequestMethod.GET)
  public List<User> findAll() {
    List<User> users = UserService.findAll();
    return users;
  }

  /***
   * @Author: aiYunS
   * @Description: 测试多线程异步及其注解@Async @EnableAsync
   *               UserServiceImpl层开启异步,结果封装到Future类返回
   * @Date: 2023-1-11 10:13
   * @Param:
   * @return: java.util.List<com.aiyuns.tinkerplay.entity.User>
   */
  @Operation(summary = "查询所有_测试多线程返回结果")
  @WebLog(description = "查询所有_测试多线程返回结果")
  @RequestMapping(value = "findAll2", method = RequestMethod.GET)
  public List<User> findAll2() {
    Future<List<User>> users = UserService.findAll2();
    try {
      System.out.println("测试定时任务返回: " + users.get());
      return users.get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }

  /***
   * @Author: aiYunS
   * @Description: 使用insertOne方法册测试延时双删;因为UserServiceImpl使用findByName方法先查询数据是否存在,
   *               所以UserController的insertOne入口处为@Decrypt(description = "findByName")
   *               会模糊寻找redis缓存key为findByName的缓存
   * @Date: 2022-9-27 下午 05:01
   * @param user
   * @return: com.aiyuns.tinkerplay.entity.R
   */
  @Operation(summary = "插入一条_加密参数")
  @WebLog(description = "插入一条")
  @RequestMapping(value = "insertOne", method = RequestMethod.POST, produces = "application/json")
  @Decrypt(description = "findByName")
  @ClearAndReloadCache(name = "findByName")
  public R insertOne(@Parameter(name = "User") @RequestBody User user) {
    return R.ok("ok", UserService.insertOne(user));
  }

  /***
   * @Author: aiYunS
   * @Description: 使用insertOne方法册测试延时双删;因为UserServiceImpl使用findByName方法先查询数据是否存在,
   *               所以UserController的insertOne入口处为@Decrypt(description = "findByName")
   *               会模糊寻找redis缓存key为findByName的缓存
   * @Date: 2022-9-27 下午 05:01
   * @param user
   * @return: com.aiyuns.tinkerplay.entity.R
   */
  @Operation(summary = "插入一条_测试MQ延迟删除")
  @WebLog(description = "插入一条_测试MQ延迟删除")
  @RequestMapping(value = "insertOne2", method = RequestMethod.POST, produces = "application/json")
  @Decrypt(description = "findByName")
  @ClearAndReloadCache(name = "findByName")
  public R insertOne2(@Parameter(name = "User") @RequestBody User user) {
    return R.ok("ok", UserService.insertOne2(user));
  }

  @Operation(summary = "插入一条_测试新增获取ID")
  @WebLog(description = "插入一条_测试新增获取ID")
  @RequestMapping(value = "insertOne3", method = RequestMethod.POST, produces = "application/json")
  @ClearAndReloadCache(name = "findByName")
  public R insertOne3(@Parameter(name = "User") @RequestBody User user) {
    return R.ok("ok", UserService.insertOne3(user));
  }

  @Operation(summary = "根据名字删除")
  @WebLog(description = "根据名字删除")
  @RequestMapping(value = "deleteByName", method = RequestMethod.POST)
  public void deleteByName(
      @Parameter(name = "QueryRequestVo") @RequestBody QueryRequestVo queryRequestVo) {
    UserService.deleteByName(queryRequestVo);
  }

  @Operation(summary = "更新一条数据")
  @WebLog(description = "更新一条数据")
  @RequestMapping(value = "updateOne", method = RequestMethod.POST)
  @ClearAndReloadCache(name = "findById") // 依旧测试延时双删
  public void updateOne(
      @Parameter(name = "QueryRequestVo") @RequestBody QueryRequestVo queryRequestVo) {
    UserService.updateOne(queryRequestVo);
  }

  /*
   * @Author: aiYunS
   * @Description: @Nullable 注解可以标注在方法、字段、参数之上，表示对应的值可以为空
   * @Date: 2022-9-15 上午 10:38
   * @Param:
   * @param null
   * @return: User
   */
  @Operation(summary = "根据ID查询")
  @WebLog(description = "根据ID查询")
  @RequestMapping(value = "findById", method = RequestMethod.POST)
  @Encrypt(description = "根据ID查询加密查询结果")
  public R findById(
      @Parameter(name = "QueryRequestVo") @RequestBody @NullDisable QueryRequestVo queryRequestVo) {
    return R.ok("ok", UserService.findById(queryRequestVo));
  }

  /*
   * @Author: aiYunS
   * @Description: 测试HttpUtil和MacUtil工具类,Swagger测试
   * @Date: 2022-9-15 上午 10:32
   * @Param: QueryRequestVo
   * @return: User
   */
  @Operation(summary = "根据名字查询")
  @WebLog(description = "根据名字查询")
  @RequestMapping(value = "findByName", method = RequestMethod.POST)
  public User findByName(
      @Parameter(name = "QueryRequestVo") @RequestBody QueryRequestVo queryRequestVo) {
    return UserService.findByName(queryRequestVo);
  }

  /*
   * @Author: aiYunS
   * @Description: 测试BI组件与后端交互
   *               @CrossOrigin注解用于解决跨域问题
   * @Date: 2022-9-15 上午 10:39
   * @Param: QueryRequestVo
   * @return: List<User>
   */
  @Operation(summary = "根据时间段查询")
  @WebLog(description = "根据时间段查询")
  @RequestMapping(value = "findByTime", method = RequestMethod.POST)
  @CrossOrigin(value = "http://localhost:10104") // 该注解解决跨域问题
  public List<User> findByTime(
      @Parameter(name = "QueryRequestVo") @RequestBody QueryRequestVo queryRequestVo) {
    List<User> users = UserService.findByTime(queryRequestVo);
    return users;
  }

  /*
   * @Author: aiYunS
   * @Description: 测试行转列RowConvertColUtil工具类
   * @Date: 2022-9-15 上午 10:40
   * @Param: QueryRequestVo
   * @return: RowConvertColUtil.ConvertData
   */
  @Operation(summary = "行转列Test")
  @WebLog(description = "行转列Test")
  @RequestMapping(value = "RowConvertCol", method = RequestMethod.POST)
  public RowConvertColUtil.ConvertData RowConvertCol(
      @Parameter(name = "QueryRequestVo") @RequestBody QueryRequestVo queryRequestVo) {
    RowConvertColUtil.ConvertData users = UserService.RowConvertCol(queryRequestVo);
    ;
    return users;
  }

  /*
   * @Author: aiYunS
   * @Description: 测试批量插入功能和单表插入的效率
   *               @Param的作用就是给参数命名，比如在mapper里面某方法A（int id），当添加注解后A（@Param("userId") int id），
   *               也就是说外部想要取出传入的id值，只需要取它的参数名userId就可以了。将参数值传如SQL语句中，通过#{userId}进行取值给SQL的参数赋值。
   * @Date: 2022-9-15 上午 10:41
   * @Param: List<User>
   * @return: void
   */
  @Operation(summary = "批量插入")
  @WebLog(description = "批量插入")
  @RequestMapping(value = "insertAll", method = RequestMethod.POST)
  public void insertAll(
      @Parameter(name = "List<User>") @Param("User") @RequestBody List<User> userList) {
    UserService.insertAll(userList);
  }

  /*
   * @Author: aiYunS
   * @Description: 测试ExcelUtil工具类
   *               测试导出查询数据为Excel功能
   *               测试阿里开源工具包easyExcle
   * @Date: 2022-9-15 上午 10:44
   * @Param: HttpServletResponse
   * @return: String
   */
  @Operation(summary = "导出为Excel")
  @WebLog(description = "导出为Excel")
  @RequestMapping(value = "downloadexcel", method = RequestMethod.GET)
  @ResponseBody
  public void getExcel(HttpServletResponse response)
      throws IllegalAccessException, IOException, InstantiationException {
    Future<List<User>> list = UserService.findAll2();
    try {
      ExcelUtil.download(response, User.class, list.get(), "Test", "xlsx");
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @Operation(summary = "获取IP或MAC地址")
  @WebLog(description = "获取IP或MAC地址")
  @RequestMapping(
      value = "getIPorMACaddress",
      method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.OPTIONS})
  public Map getIPorMACaddress(HttpServletRequest request) {
    try {
      return UserService.getIPorMACaddress(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Operation(summary = "消除转义")
  @WebLog(description = "消除转义")
  @RequestMapping(value = "removeESC", method = RequestMethod.POST)
  public ResultMsg removeESC() {
    return UserService.removeESC();
  }

  @Operation(summary = "递归_文件搜索")
  @WebLog(description = "递归_文件搜索")
  @RequestMapping(value = "recursion", method = RequestMethod.GET)
  public TreeSet recursion(
      @Parameter(name = "path", description = "路径") String path,
      @Parameter(name = "searchContent", description = "文件名") String searchContent) {
    // 先行判断上一次查询的内容与本次查询的是否一致
    RecursiveAlgorithm.clean(searchContent);
    TreeSet<String> filePath = RecursiveAlgorithm.fileSearch(new File(path), searchContent);
    return filePath;
  }
}
