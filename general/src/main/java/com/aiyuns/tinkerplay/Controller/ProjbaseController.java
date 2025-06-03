package com.aiyuns.tinkerplay.Controller;

import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.ProjBaseDao;
import com.aiyuns.tinkerplay.Entity.Projbase;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: aiYunS @Date: 2022-12-5 16:30 @Description: 控制器
 */
@RestController
@RequestMapping(value = "ProjbaseController", produces = "application/json;charset=UTF-8")
@Tag(name = "ProjbaseController", description = "分页查询模块")
public class ProjbaseController {

  // 此处控制器层直接注入dao层,没有注入业务层
  @Autowired ProjBaseDao projBaseDao;

  @Operation(summary = "分页查询_数据量大")
  @GetMapping("findBypaging1")
  public PageInfo<Projbase> findByPaging1(
      @Parameter(name = "pageNum", description = "页码") Integer pageNum,
      @Parameter(name = "pageSize", description = "每页数量") Integer pageSize) {
    long startime = System.currentTimeMillis();
    PageInfo<Projbase> pageInfo = null;
    try {
      ExecutorService executor = Executors.newSingleThreadExecutor();
      // 创建一个Callable任务
      Callable<Long> callable =
          () -> {
            return projBaseDao.CountProjbase();
          };
      // 将Callable任务包装到FutureTask中
      Future<Long> future = executor.submit(callable);
      // 全量数据大,导致SELECT count(0) FROM projbase查询总数超时,暂时关闭
      PageHelper.startPage(pageNum, pageSize, false);
      List<Projbase> list = projBaseDao.selectProjbase1();
      pageInfo = new PageInfo<>(list);
      // 设置查询总条数
      pageInfo.setTotal(future.get(1, TimeUnit.MINUTES));
      System.out.println((System.currentTimeMillis() - startime) / 1000 + " 秒程序执行完成");
      return pageInfo;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      return pageInfo;
    }
  }

  @Operation(summary = "分页查询_数据量小")
  @GetMapping("findBypaging2")
  public PageInfo<Projbase> findByPaging2(
      @Parameter(name = "pageNum", description = "页码") Integer pageNum,
      @Parameter(name = "pageSize", description = "每页数量") Integer pageSize) {

    PageHelper.startPage(pageNum, pageSize);
    List<Projbase> list = projBaseDao.selectProjbase2();
    PageInfo<Projbase> pageInfo = new PageInfo<>(list);
    System.out.println("当前页：" + pageInfo.getPageNum());
    System.out.println("每页显示的数量：" + pageInfo.getPageSize());
    System.out.println("当前页的数量：" + pageInfo.getSize());
    System.out.println("总记录数：" + pageInfo.getTotal());
    System.out.println("总页数：" + pageInfo.getPages());
    System.out.println("结果集:" + pageInfo.getList());
    System.out.println("第一页:" + pageInfo.getNavigateFirstPage());
    System.out.println("前一页:" + pageInfo.getPrePage());
    System.out.println("下一页:" + pageInfo.getNextPage());
    System.out.println("最后一页:" + pageInfo.getNavigateLastPage());
    System.out.println("是否为第一页：" + pageInfo.isIsFirstPage());
    System.out.println("是否为最后一页：" + pageInfo.isIsLastPage());
    System.out.println("是否有前一页：" + pageInfo.isHasPreviousPage());
    System.out.println("是否有下一页：" + pageInfo.isHasNextPage());
    System.out.println("导航页码数" + pageInfo.getNavigatePages());
    System.out.println("所有导航页号" + Arrays.toString(pageInfo.getNavigatepageNums()));
    return pageInfo;
  }
}
