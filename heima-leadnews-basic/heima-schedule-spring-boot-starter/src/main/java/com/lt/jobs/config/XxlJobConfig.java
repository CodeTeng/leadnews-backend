package com.lt.jobs.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/30 13:38
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(XxJobConfigProperties.class)
public class XxlJobConfig {
    @Autowired
    private XxJobConfigProperties xxJobConfigProperties;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxJobConfigProperties.getAdminAddress());
        xxlJobSpringExecutor.setAppname(xxJobConfigProperties.getExecutorName());
        xxlJobSpringExecutor.setPort(xxJobConfigProperties.getExecutorPort());
        xxlJobSpringExecutor.setLogRetentionDays(30);
        xxlJobSpringExecutor.setLogPath(xxJobConfigProperties.getLogPath());
        xxlJobSpringExecutor.setAddress(xxJobConfigProperties.getExecutorAddress());
        return xxlJobSpringExecutor;
    }
}
