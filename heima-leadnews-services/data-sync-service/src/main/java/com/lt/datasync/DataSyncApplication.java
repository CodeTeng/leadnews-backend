package com.lt.datasync;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 15:43
 */
@SpringBootApplication
@MapperScan("com.lt.datasync.mapper")
public class DataSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSyncApplication.class, args);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
