package com.aiyuns.tinkerplay.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Author: aiYunS @Date: 2022-10-10 上午 10:58 @Description: 执行定时任务的线程池配置类
 */
@Configuration
public class SchedulingConfig {

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    // 定时任务执行线程池核心线程数
    taskScheduler.setPoolSize(4);
    taskScheduler.setRemoveOnCancelPolicy(true);
    taskScheduler.setThreadNamePrefix("TaskSchedulerThreadPool-");
    return taskScheduler;
  }
}
