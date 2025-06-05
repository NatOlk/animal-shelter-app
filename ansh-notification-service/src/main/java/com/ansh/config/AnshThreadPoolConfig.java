package com.ansh.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AnshThreadPoolConfig {

  @Bean(name = "emailNotificationExecutor")
  public Executor emailNotificationExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(30);
    executor.setMaxPoolSize(100);
    executor.setQueueCapacity(1000);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setThreadNamePrefix("EmailNotification-");
    executor.initialize();
    return executor;
  }
}
