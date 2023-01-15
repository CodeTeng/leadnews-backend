package com.lt.config;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 14:11
 */
@Configuration
@EnableFeignClients(basePackages = "com.lt.feigns")
@ComponentScan("com.lt.feigns.fallback")
public class HeimaFeignAutoConfiguration {
    @Bean
    Logger.Level level() {
        return Logger.Level.FULL;
    }
}
