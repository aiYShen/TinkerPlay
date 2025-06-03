package com.aiyuns.tinkerplay.Aop;

import com.aiyuns.tinkerplay.CustomAnnotations.ClearAndReloadCache;
import com.aiyuns.tinkerplay.Entity.QueryRequestVo;
import com.aiyuns.tinkerplay.Entity.User;
import com.aiyuns.tinkerplay.Utils.redisKeyUtil;
import java.lang.reflect.Method;
import java.util.Set;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2022-9-27 上午 10:32 @Description: 延时双删切面
 */
@Aspect
@Component
public class ClearAndReloadCacheAspect {
  @Autowired private StringRedisTemplate stringRedisTemplate;

  /** 切入点 切入点,基于注解实现的切入点 加上该注解的都是Aop切面的切入点 */
  @Pointcut("@annotation(com.aiyuns.tinkerplay.CustomAnnotations.ClearAndReloadCache)")
  public void pointCut() {}

  /**
   * 环绕通知 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
   * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
   *
   * @param proceedingJoinPoint
   */
  @Around("pointCut()")
  public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
    System.out.println("----------- 环绕通知 -----------");
    System.out.println("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());

    Signature signature1 = proceedingJoinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature1;
    // 方法对象
    Method targetMethod = methodSignature.getMethod();
    // 反射得到自定义注解的方法对象
    ClearAndReloadCache annotation = targetMethod.getAnnotation(ClearAndReloadCache.class);
    // 获取自定义注解的方法对象的参数即name
    String name = annotation.name();

    /** 完善改造尽量根据redis缓存精确的key去精确删除 */
    Object[] args = proceedingJoinPoint.getArgs();
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof com.aiyuns.tinkerplay.Entity.User) {
        User user = (User) args[i];
        name = redisKeyUtil.redisKey(name, user, args);
      } else if (args[i] instanceof com.aiyuns.tinkerplay.Entity.QueryRequestVo) {
        QueryRequestVo queryRequestVo = (QueryRequestVo) args[i];
        name = redisKeyUtil.redisKey(name, queryRequestVo, args);
      }
    }
    // 模糊定义key
    Set<String> keys = stringRedisTemplate.keys("*" + name + "*");
    // 模糊删除redis的key值,延时双删第一删
    stringRedisTemplate.delete(keys);
    // 执行加入双删注解的改动数据库的业务 即controller中的方法业务
    Object proceed = null;
    try {
      proceed = proceedingJoinPoint.proceed();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }

    // 开一个线程 延迟1分（此处是5秒测试，可以改成自己的业务）
    // 在线程中延迟删除  同时将业务代码的结果返回 这样不影响业务代码的执行
    // Set<String> keys1 = keys;
    String name2 = name;
    new Thread(
            () -> {
              try {
                Thread.sleep(5000);
                // 模糊删除
                Set<String> keys1 = stringRedisTemplate.keys("*" + name2 + "*");
                // 延时双删第二删
                stringRedisTemplate.delete(keys1);
                System.out.println("-----------5秒钟后，在线程中延迟删除完毕(延时双删) -----------");
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();
    // 返回业务代码的值
    return proceed;
  }
}
