package com.lt.search.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 13:33
 */
@Data
@EnableAsync
@Configuration
public class ThreadPoolConfig {
    /**
     * 核心线程数（默认线程数）
     */
    private static final int corePoolSize = 10;
    /**
     * 最大线程数
     */
    private static final int maxPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int keepAliveTime = 10;
    /**
     * 缓冲队列数
     */
    private static final int queueCapacity = 500;

    /**
     * 默认异步线程池
     */
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setThreadNamePrefix("--------------全局线程池-----------------");
        pool.setCorePoolSize(corePoolSize);
        pool.setMaxPoolSize(maxPoolSize);
        pool.setKeepAliveSeconds(keepAliveTime);
        pool.setQueueCapacity(queueCapacity);
        // 直接在execute方法的调用线程中运行
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        pool.initialize();
        return pool;
    }
}
