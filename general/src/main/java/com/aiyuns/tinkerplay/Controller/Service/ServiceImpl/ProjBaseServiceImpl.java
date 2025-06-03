package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl;

import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.ProjBaseDao;
import com.aiyuns.tinkerplay.Entity.Projbase;
import com.aiyuns.tinkerplay.Entity.ProjbaseException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author: aiYunS @Date: 2022-12-5 14:04 @Description: 业务实现层 直接实现DAO层接口
 */
@Service
public class ProjBaseServiceImpl implements ProjBaseDao {

  @Resource ProjBaseDao projBaseDao;

  public ProjBaseServiceImpl(ProjBaseDao projBaseDao) {
    this.projBaseDao = projBaseDao;
  }

  @Override
  public long CountProjbase() {
    return projBaseDao.CountProjbase();
  }

  @Override
  public Long selectCarArchivesList_COUNT() {
    return projBaseDao.selectCarArchivesList_COUNT();
  }

  @Override
  public List<Projbase> readProjbase(Map map) {
    // 获取总记录条数
    long count = selectCarArchivesList_COUNT();
    // 设置默认参数,保证程序不报错
    if (map != null && map.size() > 0) {
      if (map.get("start") == null || "".equals(map.get("start"))) {
        map.put("start", 0);
      }
      if (map.get("end") == null || "".equals(map.get("end"))) {
        map.put("end", count);
      }
    } else {
      map.put("start", 0);
      map.put("end", count);
    }
    // 循环次数
    long cycleIndex = 1;
    // 每次批量插入100万条数据,根据总记录条数计算要循环的插入次数
    if (count > 0) {
      if (count % 1000000 == 0) {
        cycleIndex =
            count
                / (Long.parseLong(map.get("end").toString())
                    - Long.parseLong(map.get("start").toString()));
      } else {
        cycleIndex =
            count
                    / (Long.parseLong(map.get("end").toString())
                        - Long.parseLong(map.get("start").toString()))
                + 1;
      }
    } else {
      throw new RuntimeException("projbase表记录为空!!!");
    }
    for (int i = 1; i <= cycleIndex; i++) {
      List<Projbase> projbaseList = null;
      List<ProjbaseException> projbaseExceptionList = new ArrayList<ProjbaseException>();
      ProjbaseException projbaseException;
      // 固定每次查询的条数,循环查询
      if (i != 1) {
        long a =
            (Long.parseLong(map.get("end").toString())
                - Long.parseLong(map.get("start").toString()));
        long newStart = (Long.parseLong(map.get("end").toString()) + 1);
        map.put("start", newStart);
        map.put("end", newStart + a);
      }
      projbaseList = projBaseDao.readProjbase(map);
      // 记录最后一条数据的projid
      //           String jsonObj = projbaseList.get(projbaseList.size() - 1).getJsonObj();
      //           JSONObject JSONObject = JSON.parseObject(jsonObj);
      //           String lastProjid = JSONObject.getString("projId");
      for (int j = 0; j < projbaseList.size(); j++) {
        projbaseException = new ProjbaseException();
        JSONObject obj = JSON.parseObject(projbaseList.get(j).getJsonObj());
        // 获取登录人名称
        String recvUserName = "";
        if (obj.getString("recvUserName") != null) {
          recvUserName = obj.getString("recvUserName");
        }
        // 获取查询人姓名
        JSONObject affFormInfo = JSON.parseObject(obj.getString("affFormInfo"));
        String sqrname = "";
        if (affFormInfo.getString("sqrname") != null) {
          sqrname = affFormInfo.getString("sqrname");
        }
        // 判断不一致信息
        if (!recvUserName.equals(sqrname) && sqrname.length() < 7) {
          System.out.println("第" + i + "次循环的, 第" + j + "条数据");
          projbaseException.setProjId(obj.getString("projId"));
          if (projbaseList.get(j).getGmtApply() != null
              && !"".equals(projbaseList.get(j).getGmtApply())) {
            projbaseException.setCjsj(projbaseList.get(j).getGmtApply());
          } else {
            if (projbaseList.get(j).getCjsj() != null
                && !"".equals(projbaseList.get(j).getCjsj())) {
              projbaseException.setCjsj(projbaseList.get(j).getCjsj().toString());
            } else {
              projbaseException.setCjsj(null);
            }
          }
          projbaseException.setProjectName(obj.getString("projectName"));
          projbaseException.setApplyName(affFormInfo.getString("sqrname"));
          projbaseException.setApplyCardNo(affFormInfo.getString("zjh"));
          projbaseException.setQxdm(affFormInfo.getString("xzqbm"));
          projbaseException.setRecvUserName(obj.getString("recvUserName"));
          projbaseException.setRecvDeptCode(obj.getString("recvDeptName"));
          projbaseException.setRecvUserId(obj.getString("recvUserId"));
          projbaseException.setFaceValidationResult(obj.getString("faceValidationResult"));
          if (projbaseException != null) {
            projbaseExceptionList.add(projbaseException);
          }
        }
      }
      if (projbaseExceptionList != null && projbaseExceptionList.size() != 0) {
        writeProjbaseException(projbaseExceptionList);
        // 提示垃圾回收器回收内存
        projbaseList = null;
        projbaseExceptionList = null;
        projbaseException = null;
        System.gc();
      }
    }
    return null;
  }

  @Override
  @Async("asyncServiceExecutor")
  public void writeProjbaseException(List list) {
    projBaseDao.writeProjbaseException(list);
  }

  @Override
  public Page<Projbase> selectProjbase1() {
    return projBaseDao.selectProjbase1();
  }

  @Override
  public Page<Projbase> selectProjbase2() {
    return projBaseDao.selectProjbase2();
  }
}
