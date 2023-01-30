package com.lt.article.listen;

import com.lt.common.constants.article.HotArticleConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: 用于将监听到的行为数据 存入到redis队列中 等待计算
 * @author: ~Teng~
 * @date: 2023/1/30 15:09
 */
@Component
@Slf4j
public class HotArticleScoreListener {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queuesToDeclare = @Queue(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE))
    public void articleBehaviorHandler(String behaviorMess) {
        log.info(" 文章实时更新队列: 接收到文章行为变化 消息内容: {}", behaviorMess);
        try {
            stringRedisTemplate.opsForList().rightPush(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_LIST, behaviorMess);
            log.info("文章实时更新队列成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文章实时更新队列， 消息处理失败: {}   失败原因: {}", behaviorMess, e.getMessage());
        }
    }
}
