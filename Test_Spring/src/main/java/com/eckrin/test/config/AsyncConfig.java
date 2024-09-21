package com.eckrin.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncConfig {

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20); // 초기 스레드 풀 크기
        executor.setMaxPoolSize(20); // 최대 스레드 풀 크기
        executor.setQueueCapacity(300000); // 대기열 크기
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // reject시 처리정책 - 시간이 걸려도 처리
        executor.initialize();
        return executor;
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }
}
