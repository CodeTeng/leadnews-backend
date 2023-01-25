package com.lt.article.listen;

import com.lt.article.service.ApArticleService;
import com.lt.common.constants.message.PublishArticleConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/25 18:17
 */
@Component
@Slf4j
public class PublishArticleListener {
    @Autowired
    private ApArticleService apArticleService;

    @RabbitListener(queuesToDeclare = @Queue(PublishArticleConstants.PUBLISH_ARTICLE_QUEUE))
    public void publishArticle(String newsId) {
        log.info("接收到发布文章通知， 待发布文章id: {} , 当前时间: {}", newsId, LocalDateTime.now());
        try {
            apArticleService.publishArticle(Integer.valueOf(newsId));
            log.info("发布文章通知处理完毕  文章发布成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发布文章通知处理失败， 文章未能成功发布 文章id: {} , 失败原因:{}", newsId, e.getMessage());
        }
    }
}
