package com.aiyuns.tinkerplay.Config;

import com.aiyuns.tinkerplay.Controller.Service.RedisService;
import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.RedisServiceImpl;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis管理器 自定义redis配置
 *
 * @author redis缓存配置类
 */
@Configuration
public class RedisCacheConfig implements CachingConfigurer {
  /**
   * 默认Redis全局配置。（30分钟超时）
   *
   * @param redisConnectionFactory
   * @return
   */
  @Primary
  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(instanceConfig(30L))
        .build();
  }

  /** 永不超时 */
  @Bean
  public RedisCacheManager noExpire(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(0L)).build();
  }

  /** 1分钟超时 用于测试 */
  @Bean
  public RedisCacheManager expire1min(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(1L)).build();
  }

  /** 2小时超时 */
  @Bean
  public RedisCacheManager expire2h(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(instanceConfig(60 * 2L))
        .build();
  }

  /** 一天超时 */
  @Bean
  public RedisCacheManager expire1day(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(instanceConfig(60 * 24L))
        .build();
  }

  /**
   * 通用配置
   *
   * @param ttl 超时时间（分钟）
   */
  private RedisCacheConfiguration instanceConfig(Long ttl) {
    // 缓存配置对象
    return RedisCacheConfiguration.defaultCacheConfig()
        // 设置缓存的默认超时时间
        .entryTtl(Duration.ofMinutes(ttl))
        // 如果是空值，不缓存（不建议设置，防止缓存穿透）
        // .disableCachingNullValues()
        // 设置key序列化器
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer(StandardCharsets.UTF_8)))
        // 设置value序列化器（这里使用阿里巴巴的Fastjson格式化工具）
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericFastJsonRedisSerializer()));
  }

  /*
   * @Author: aiYunS
   * @Description: 自定义redis缓存的key
   * @Date: 2022-7-13 下午 03:37
   * @Param:
   * @param null
   * @return:
   */
  @Override
  public KeyGenerator keyGenerator() {
    return new KeyGenerator() {
      @Override
      public Object generate(Object o, Method method, Object... objects) {
        // 自定义缓存key字符串
        StringBuilder sb = new StringBuilder();
        boolean isappen = false;
        // 追加类名
        // String className = o.getClass().getName();
        // className = className.substring(className.lastIndexOf(".")+1);
        // sb.append(className + "_");
        // 追加方法名
        for (Object o1 : objects) {
          if (o1 != null) {
            isappen = true;
          }
        }
        sb.append(method.getName());
        if (objects.length != 0 && isappen) {
          sb.append(":");
          // 遍历参数并且追加
          for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            String strObj = null;
            if (obj instanceof java.util.Date) {
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              strObj = sdf.format(obj).toString().replace("-", "");
              if (i == objects.length - 1) {
                sb.append(strObj);
              } else {
                sb.append(strObj);
                sb.append("_");
              }
            } else {
              if (i == objects.length - 1) {
                sb.append(obj.toString());
              } else {
                sb.append(obj.toString());
                sb.append("_");
              }
            }
          }
        }
        // System.out.println("Redis缓存的Key : " + sb.toString());
        return sb.toString();
      }
    };
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisSerializer<Object> serializer = redisSerializer();
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(serializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean
  public RedisSerializer<Object> redisSerializer() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
    objectMapper.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    // serializer.setObjectMapper(objectMapper);
    // 创建JSON序列化器
    Jackson2JsonRedisSerializer<Object> serializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    return serializer;
  }

  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheWriter redisCacheWriter =
        RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
    // 设置Redis缓存有效期为1天
    RedisCacheConfiguration redisCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()))
            .entryTtl(Duration.ofDays(1));
    return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
  }

  @Bean
  public RedisService redisService() {
    return new RedisServiceImpl();
  }
}
