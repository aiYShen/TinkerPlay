package com.aiyuns.tinkerplay.Controller;

import com.aiyuns.tinkerplay.Controller.Service.TokenAccessService;
import com.aiyuns.tinkerplay.CustomAnnotations.WebLog;
import com.aiyuns.tinkerplay.Entity.TokenAccess;
import com.aiyuns.tinkerplay.Utils.ResolveTokenUtil;
import com.aiyuns.tinkerplay.Utils.TokenCreateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: aiYunS @Date: 2022-10-12 下午 04:09 @Description: 测试通过解析Token,查询数据库数据的权限状态,并返回状态
 */
@RestController
@RequestMapping(value = "AccessControl", produces = "application/json;charset=UTF-8")
@Tag(name = "TokenAccessController", description = "Token校验模块")
public class TokenAccessController {

  @Autowired TokenAccessService tokenAccessService;

  @Operation(summary = "解析Token校验权限")
  @WebLog(description = "权限检查")
  @RequestMapping(
      value = "check",
      method = {RequestMethod.GET, RequestMethod.POST},
      produces = "application/json")
  @CrossOrigin
  public JSONObject check(HttpServletRequest request) {
    JSONObject data = new JSONObject();
    TokenAccess tokenAccess = ResolveTokenUtil.resolveToken(request.getHeader("x-gisq-token"));
    int status = tokenAccessService.checkStatus(tokenAccess);
    data.put("status", status);
    return data;
  }

  @Operation(summary = "获取登录Token")
  @WebLog(description = "获取登录Token")
  @RequestMapping(value = "/jwt/sign", method = RequestMethod.POST)
  @CrossOrigin
  public String sign(@RequestBody String request) {
    JSONObject jsonObject = JSON.parseObject(request);
    String username = jsonObject.getString("username");
    String password = jsonObject.getString("password");
    return TokenCreateUtil.token(username, password);
  }
}
