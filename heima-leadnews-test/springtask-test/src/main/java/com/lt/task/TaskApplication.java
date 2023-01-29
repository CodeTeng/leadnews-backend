package com.lt.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 21:27
 */
@SpringBootApplication
@EnableScheduling   // 开启定时任务
public class TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
