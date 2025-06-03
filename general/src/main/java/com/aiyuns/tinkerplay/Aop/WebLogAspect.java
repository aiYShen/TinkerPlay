package com.aiyuns.tinkerplay.Aop;

import com.aiyuns.tinkerplay.CustomAnnotations.WebLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: aiYunS @Date: 2022-7-27 下午 02:39 @Description: 日志输出切面类
 */
@Aspect
@Component
public class WebLogAspect {

  private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
  // 换行符
  private static final String LINE_SEPARATOR = System.lineSeparator();
  private Object result = null;
  private long startTime = 0;

  /*
   * @Author: aiYunS
   * @Date: 2022-7-27 下午 02:43
   * @Description: 以自定义 @WebLog 注解为切点
   */
  // 切点方法
  @Pointcut("@annotation(com.aiyuns.tinkerplay.CustomAnnotations.WebLog)")
  public void WebLog() {}
  ;

  /*
   * @Author: aiYunS
   * @Description:定义 @Around 环绕，用于何时执行切点
   * @Date: 2022-7-27 下午 02:43
   * @Param:
   * @return:
   */
  // 与切点方法名称要一致
  @Around("WebLog()")
  public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

    // result = check(proceedingJoinPoint);//先进行入参为空的校验
    startTime = System.currentTimeMillis();
    result = proceedingJoinPoint.proceed();
    // 打印出参
    // logger.info("Response Args : {}",new Gson().toJson(result));
    // 执行耗时
    // logger.info("Time-Consuming : {} ms",System.currentTimeMillis() - startTime);
    return result;
  }

  /*
   * @Author: aiYunS
   * @Description: 切点之前执行
   * @Date: 2022-7-27 下午 03:32
   * @Param:
   * @return:
   */
  @Before("WebLog()")
  public void doBefore(JoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();
    Object[] arguments = new Object[args.length];
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof ServletRequest
          || args[i] instanceof ServletResponse
          || args[i] instanceof MultipartFile) {
        // ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call
        // this method if the current request is not in asynchronous mode (i.e. isAsyncStarted()
        // returns false)
        // ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has
        // already been called for this response
        continue;
      }
      arguments[i] = args[i];
    }
    String paramter = "";
    if (arguments != null) {
      try {
        paramter = JSONObject.toJSONString(arguments);
      } catch (Exception e) {
        paramter = arguments.toString();
      }
    }
    // 开始打印请求日志
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    // 因为测试定时任务,并非是正常的请求进来,导致RequestContextHolder.getRequestAttributes()获取不到信息
    if (attributes == null) {
      // 这里为了处理定时任务不报空指针,不做任何处理,定时任务的日志输出放在了定时任务执行时
    } else {
      HttpServletRequest request = attributes.getRequest();
      // 获取 @WebLog 注解的描述信息
      String methodDescription = getAspectLogDescription(joinPoint);
      // 打印请求相关参数
      logger.info("====================== Start =====================");
      // 打印请求 url
      logger.info("URL           : {}", request.getRequestURL().toString());
      // 打印描述信息
      logger.info("Description   : {}", methodDescription);
      // 打印 HTTP Method
      logger.info("HTTP Method   : {}", request.getMethod());
      // 打印调用 controller 的全路径以及执行方法
      logger.info(
          "Class Method  : {}.{}",
          joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName());
      // 打印请求的 IP
      logger.info("IP            : {}", request.getRemoteAddr());
      // 打印请求入参
      // logger.info("Request Args  : {}",new Gson().toJson(joinPoint.getArgs()));
      if (request.getRequestURL().toString().contains("/downloadexcel")) {
        logger.info("Request Args  : {}", "null");
      } else {
        // logger.info("Request Args  : {}",JSON.toJSONString(joinPoint.getArgs()));
        logger.info("Request Args  : {}", paramter);
      }
      // 打印出参
      // logger.info("Response Args : {}",new Gson().toJson(result));
      logger.info("Response Args : {}", JSON.toJSONString(result));
      // 执行耗时
      logger.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
    }
  }

  /*
   * @Author: aiYunS
   * @Description: 在切点之后执行
   * @Date: 2022-7-27 下午 04:42
   * @Param:
   * @return:
   */
  @After("WebLog()")
  public void doAfter() throws Throwable {
    // 接口结束后换行,分割查看
    logger.info("====================== End =======================");
  }

  /** 获取切面注解的描述 */
  private String getAspectLogDescription(JoinPoint joinPoint) throws Exception {
    String targetName = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] arguments = joinPoint.getArgs();
    Class targetClass = Class.forName(targetName);
    Method[] methods = targetClass.getMethods();
    StringBuilder description = new StringBuilder();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Class[] clazz = method.getParameterTypes();
        if (clazz.length == arguments.length) {
          description.append(method.getAnnotation(WebLog.class).description());
          break;
        }
      }
    }
    return description.toString();
  }

  // 检查入参
  //    public static Object check(ProceedingJoinPoint joinPoint) throws Throwable {
  //        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
  //        Method method = signature.getMethod();
  //        //获得参数类型
  //        final Parameter[] parameters = method.getParameters();
  //        //参数值
  //        final Object[] args = joinPoint.getArgs();
  //        //参数名称
  //        String[] names = signature.getParameterNames();
  //
  //
  //        for (int i = 0; i < parameters.length; i++) {
  //            Parameter parameter = parameters[i];
  //            Object annotation = parameter.getAnnotation(NullDisable.class);
  //            //含有不为空的注解的参数
  //            if (null != annotation) {
  //                if (null == args[i]) {
  //                    throw new ParamException(String.format("参数:%s,不能为空", names[i]));
  //                }
  //            }
  //
  //        }
  //        return joinPoint.proceed();
  //    }
}
