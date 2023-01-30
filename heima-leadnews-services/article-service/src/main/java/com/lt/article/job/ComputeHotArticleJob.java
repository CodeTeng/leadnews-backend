package com.lt.article.job;

import com.lt.article.service.HotArticleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/30 13:42
 */
@Component
@Slf4j
public class ComputeHotArticleJob {
    @Autowired
    private HotArticleService hotArticleService;

    @XxlJob("computeHotArticleJob")
    public ReturnT handle(String param) {
        log.info("热文章分值计算调度任务开始执行....");
        // 实现热点文章分值计算任务
        hotArticleService.computeHotArticle();
        log.info("热文章分值计算调度任务完成....");
        return ReturnT.SUCCESS;
    }
}
