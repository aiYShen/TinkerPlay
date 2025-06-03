package com.aiyuns.tinkerplay.Config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Author: aiYunS @Date: 2022-12-16 10:52 @Description: 线程池配置类
 */
@Configuration
@EnableAsync // 允许使用异步方法
@Slf4j
public class ExecutorConfig {

  /** 核心线程数(默认线程数) */
  @Value("${spring.datasource.dynamic.druid.initial-size}")
  private int corePoolSize;

  /** 最大线程数 */
  @Value("${spring.datasource.dynamic.druid.max-active}")
  private int maxPoolSize;

  /** 允许线程空闲时间（单位：默认为秒） */
  @Value("${spring.datasource.dynamic.druid.min-evictable-idle-time-millis}")
  private static final int keepAliveTime = 60;

  /** 缓冲队列大小 */
  private int queueCapacity = 10;

  @ConfigurationProperties(prefix = "spring.datasource")
  @Bean(name = "asyncServiceExecutor")
  public Executor asyncServiceExecutor() {
    log.info("start asyncServiceExecutor");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 配置核心线程数
    executor.setCorePoolSize(corePoolSize);
    // 配置最大线程数
    executor.setMaxPoolSize(maxPoolSize);
    // 配置空闲时间
    executor.setKeepAliveSeconds(keepAliveTime);
    // 配置队列大小
    executor.setQueueCapacity(queueCapacity);
    // 配置线程前缀名
    executor.setThreadNamePrefix("async-service-");

    // rejection-policy：当pool已经达到max size的时候，如何处理新任务
    // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

    // 执行初始化
    executor.initialize();
    return executor;
  }
}
