package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl;

import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.UserDao;
import com.aiyuns.tinkerplay.Controller.Service.UserService;
import com.aiyuns.tinkerplay.Entity.*;
import com.aiyuns.tinkerplay.Rabbitmq.CancelOrderSender;
import com.aiyuns.tinkerplay.Utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author: aiYunS @Date: 2021/6/30 上午 11:32 @Description:
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Resource UserDao UserDao;

  @Autowired private CancelOrderSender cancelOrderSender;

  // 测试多数据源配置注解@DS
  // @DS("slave_1")
  // @Async("asyncServiceExecutor") //开启这里会导致controller层返回为null;
  @Override
  public List<User> findAll() {
    List<User> users = UserDao.findAll0();
    //        System.out.println("=============测试定时任务查询信息并且输出开始==============");
    //        System.out.println("users" + users);
    //        System.out.println("=============测试定时任务查询信息并且输出结束==============");
    return users;
  }

  @DS("slave_1")
  @Async("asyncServiceExecutor") // 开启异步,需要把返回结果封装到AsyncResult类中;
  @Override
  public Future<List<User>> findAll2() {
    List<User> users = UserDao.findAll2();
    //        System.out.println("=============测试定时任务查询信息并且输出开始==============");
    //        System.out.println("users" + users);
    //        System.out.println("=============测试定时任务查询信息并且输出结束==============");
    System.out.println("线程: " + Thread.currentThread().getName());
    return CompletableFuture.completedFuture(users);
  }

  @Override
  public R insertOne(User user) {
    User u = new User();
    // 因为此处使用的findByName方法先查询数据是否存在,
    // 所以UserController的insertOne入口处为@Decrypt(description = "findByName")
    u = UserDao.findByName(user.getUsername());
    if (u == null) {
      UserDao.insertOne(user);
      return R.ok("ok", user);
    } else {
      return R.error("已存在编号为" + u.toString() + "的数据,请重新插入...");
    }
  }

  // 测试RabbitMQ延迟删除数据,先新增后删除
  @Override
  public R insertOne2(User user) {
    User u = UserDao.findByName(user.getUsername());
    if (u == null) {
      Map<String, Object> map = new HashMap<>();
      QueryRequestVo queryRequestVo = new QueryRequestVo();
      queryRequestVo.setUser(user);
      map.put("QueryRequestVo", queryRequestVo);
      JSONObject jsonObject = new JSONObject(map);
      UserDao.insertOne(user);
      // 新起线程,发送延迟删除的信息给RabbitMQ
      new Thread(
              () -> {
                System.out.println("线程启动,开始执行发送延迟删除的信息给RabbitMQ!");
                cancelOrderSender.sendMessage(JSON.toJSONString(jsonObject), 60 * 1000);
                System.out.println("给RabbitMQ发送延迟删除的信息完成");
              })
          .start();
      return R.ok("ok", user);
    } else {
      return R.error("已存在编号为" + u.toString() + "的数据,请重新插入...");
    }
  }

  // 测试新增获取ID
  @Override
  public R insertOne3(User user) {
    User u = UserDao.findByName(user.getUsername());
    if (u == null) {
      // 插入
      UserDao.insertOne3(user);
      int id = user.getId();
      System.out.println("新增数据的自增ID为: " + id);
      return R.ok("ok", user);
    } else {
      return R.error("已存在编号为" + u.toString() + "的数据,请重新插入...");
    }
  }

  @Override
  public void deleteByName(QueryRequestVo queryRequestVo) {
    User u = new User();
    u = UserDao.findByName(queryRequestVo.getUser().getUsername());
    if (u != null) {
      UserDao.deleteByName(queryRequestVo.getUser().getUsername());
    } else {
      throw new RuntimeException("删除的数据不存在,请重新输入...");
    }
  }

  @Override
  public void deleteByName2(QueryRequestVo queryRequestVo) {
    User u = new User();
    u = UserDao.findByName(queryRequestVo.getUser().getUsername());
    if (u != null) {
      UserDao.deleteByName(queryRequestVo.getUser().getUsername());
    } else {
      throw new RuntimeException("删除的数据不存在,请重新输入...");
    }
  }

  @Override
  public void updateOne(QueryRequestVo queryRequestVo) {
    User u = new User();
    u = UserDao.findById(queryRequestVo.getUser().getId());
    if (u != null) {
      UserDao.updateOne(queryRequestVo.getUser());
    } else {
      throw new RuntimeException("该条数据不存在,请重新输入...");
    }
  }

  @Override
  public R findById(QueryRequestVo queryRequestVo) {
    User u = UserDao.findById(queryRequestVo.getUser().getId());
    if (u == null) {
      return R.error("查询失败", u);
    }
    return R.ok("查询成功", u);
  }

  @Override
  public User findByName(QueryRequestVo queryRequestVo) {
    return UserDao.findByName(queryRequestVo.getUser().getUsername());
  }

  @Override
  public List<User> findByTime(QueryRequestVo queryRequestVo) {
    List<User> users = null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date startTime = simpleDateFormat.parse("1990-01-01");
      Date endTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
      if (queryRequestVo.getStartTime() != null && queryRequestVo.getEndTime() != null) {
        startTime = queryRequestVo.getStartTime();
        endTime = queryRequestVo.getEndTime();
        users = UserDao.findByTime(startTime, endTime);
      } else {
        users = UserDao.findAll0();
      }
      return users;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return users;
  }

  @Override
  public RowConvertColUtil.ConvertData RowConvertCol(QueryRequestVo queryRequestVo) {
    List<User> users = null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date startTime = simpleDateFormat.parse("1993-01-01");
      Date endTime = simpleDateFormat.parse("1995-12-31");
      if (queryRequestVo.getStartTime() != null && queryRequestVo.getEndTime() != null) {
        startTime = queryRequestVo.getStartTime();
        endTime = queryRequestVo.getEndTime();
        users = UserDao.findByTime(startTime, endTime);
      } else {
        users = UserDao.findAll0();
      }
      String[] fixedColumn = {"id", "username", "address", "sex", "birthday"};
      String[] fixedColumnName = {"序号", "姓名", "地址", "性别", "生日"};
      return RowConvertColUtil.doConvertReturnObj(
          users, "username", fixedColumn, "sex", true, fixedColumnName, "0");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 批量插入
  @Override
  public void insertAll(List<User> userList) {
    UserDao.insertAll(userList);
  }

  // 测试MacUtil工具类和批量查询
  @Override
  public Map getIPorMACaddress(HttpServletRequest request) throws Exception {
    Map<String, String> address = new HashMap<String, String>();
    ClientMsg requestMsg = MacUtil.getRequestMsg(request);
    String ipAddr = MacUtil.getIpAddr(request);
    String addr = MacUtil.getAddress(ipAddr);
    // String macAddress1 = MacUtil.getMACAddress(InetAddress.getByName("www.baidu.com"));
    String macAddress2 = MacUtil.getMACAddress(InetAddress.getByName(ipAddr));
    String macAddress3 = MacUtil.getMACAddress(InetAddress.getLocalHost());
    // String macAddress4 = MacUtil.getMACAddress(InetAddress.getLoopbackAddress());
    address.put("RequestMsg", requestMsg.toString());
    address.put("IpAddr", ipAddr);
    address.put("macAddress-getByName", macAddress2);
    address.put("macAddress-getLocalHost", macAddress3);
    return address;
  }

  @Override
  public ResultMsg removeESC() {
    JSONObject[] jsonObjects = ReadTXTtoJsonObjUtil.readTXTtoObj("");
    List<Projbase> projbaseList = null;
    ArrayList projIds = null;
    ArrayList ywhs = null;
    ArrayList projId = null;
    ResultMsg resultMsg = new ResultMsg();
    // 存放推送的业务号
    if (jsonObjects != null && jsonObjects.length != 0) {
      projbaseList = new ArrayList<>(jsonObjects.length);
      projIds = new ArrayList(jsonObjects.length);
      ywhs = new ArrayList(jsonObjects.length);
      for (JSONObject jsonObject : jsonObjects) {
        if (jsonObject != null) {
          Projbase projbase = new Projbase();
          projbase.setYwh(jsonObject.getString("projId"));
          projbase.setProjId(jsonObject.getString("projId"));
          projbase.setJsonObj(jsonObject.toJSONString());
          projbase.setCjsj(null);
          projbase.setMatterCode(jsonObject.getString("matterCode"));
          projbase.setProjectName(jsonObject.getString("projectName"));
          projbase.setGmtApply(jsonObject.getString("gmtApply"));
          projbase.setApplyName(jsonObject.getJSONObject("affFormInfo").getString("sqrname"));
          projbase.setApplyCardNo(jsonObject.getJSONObject("affFormInfo").getString("zjh"));
          projbase.setBdcqzh("");
          projbase.setBdcdjzmh("");
          projbase.setZl("");
          projbase.setAreaCode(jsonObject.getString("areaCode"));
          projbase.setZt(666);
          projbaseList.add(projbase);
          projIds.add(jsonObject.getString("projId"));
        }
      }
      projId = UserDao.findByprojbase(projIds);
      if (projId != null && !projId.isEmpty()) {
        // 筛选出来已存在的ywh存放到一个list
        for (int i = 0; i < projId.toArray().length; i++) {
          if (projIds.contains(projId.toArray()[i])) {
            ywhs.add(projId.toArray()[i]);
          }
        }
        // projbaseList中已存在的ywh进行剔除后在推送.
        for (Object ywh : ywhs) {
          for (int k = 0; k < projbaseList.size(); k++) {
            if (projbaseList.get(k).getProjId().equals(ywh)) {
              projbaseList.remove(k);
            }
          }
        }
      }
    }
    if (projbaseList != null && !projbaseList.isEmpty()) {
      UserDao.removeESC(projbaseList);
      // 调办结接口
      StringBuffer stringBuffer =
          ToInterfaceUtil.interfaceUtil(
              "https://govbdctj.zjzwfw.gov.cn:7079/api/right/ignoreOuth/house/callback/fillhousefinish",
              projIds.toString(),
              "POST");
      JSONObject sbObj = (JSONObject) JSON.parse(stringBuffer.toString());
      resultMsg.setStatus(sbObj.getString("status"));
      resultMsg.setMsg(sbObj.getString("msg"));
      resultMsg.setData(sbObj.getString("data"));
      resultMsg.setCode(sbObj.getString("code"));
      resultMsg.setStackTrace(sbObj.getString("stackTrace"));
      resultMsg.setRequestId(sbObj.getString("requestId"));
      resultMsg.setSuccess(sbObj.getString("success"));
      if (!ywhs.isEmpty()) {
        resultMsg.setMessage("这些数据: " + StrSpliceUtil.strSplice(ywhs) + " 已存在projbase表中!");
      } else {
        resultMsg.setMessage(sbObj.getString("message"));
      }
      return resultMsg;
    } else {
      resultMsg.setStatus("89757");
      resultMsg.setMsg("不存在可推的数据或数据已存在projbase表中!!!");
      resultMsg.setData("");
      resultMsg.setCode("-1");
      resultMsg.setStackTrace("stackTrace");
      resultMsg.setRequestId("123456789");
      resultMsg.setSuccess("false");
      if (ywhs != null && !ywhs.isEmpty()) {
        resultMsg.setMessage("这些数据: " + StrSpliceUtil.strSplice(ywhs) + " 已存在projbase表中!");
      } else {
        resultMsg.setMessage("不存在可推的数据或数据已存在projbase表中!!!");
      }
      return resultMsg;
    }
  }
}
