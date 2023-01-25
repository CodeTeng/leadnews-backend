package com.lt.article.listen;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lt.article.service.ApArticleConfigService;
import com.lt.common.constants.message.NewsUpOrDownConstants;
import com.lt.model.article.pojo.ApArticleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 文章上下架消费者
 * @author: ~Teng~
 * @date: 2023/1/25 18:43
 */
@Component
@Slf4j
public class ArticleUpOrDownListener {
    @Autowired
    private ApArticleConfigService apArticleConfigService;

    @RabbitListener(queuesToDeclare = @Queue(NewsUpOrDownConstants.NEWS_UP_FOR_ARTICLE_CONFIG_QUEUE))
    public void newsUpHandler(String articleId) {
        log.info("接收到自媒体文章上架消息, 文章id: {}", articleId);
        try {
            // 更新文章配置信息
            apArticleConfigService.update(Wrappers.<ApArticleConfig>lambdaUpdate()
                    .set(ApArticleConfig::getIsDown, false)
                    .eq(ApArticleConfig::getArticleId, Long.valueOf(articleId)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("article处理 自媒体文章上架消息 失败, 文章id: {}   原因: {}", articleId, e.getMessage());
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(NewsUpOrDownConstants.NEWS_DOWN_FOR_ARTICLE_CONFIG_QUEUE))
    public void newsDownHandler(String articleId) {
        log.info("接收到自媒体文章下架消息, 文章id: {}", articleId);
        try {
            // 更新文章配置信息
            apArticleConfigService.update(Wrappers.<ApArticleConfig>lambdaUpdate()
                    .set(ApArticleConfig::getIsDown, true)
                    .eq(ApArticleConfig::getArticleId, Long.valueOf(articleId)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("article处理 自媒体文章下架消息 失败, 文章id: {}   原因: {}", articleId, e.getMessage());
        }
    }
}
