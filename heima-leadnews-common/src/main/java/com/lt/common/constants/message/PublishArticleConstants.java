package com.lt.common.constants.message;

/**
 * @description: 发布文章相关常量
 * @author: ~Teng~
 * @date: 2023/1/25 17:55
 */
public interface PublishArticleConstants {
    /**
     * 发布文章队列
     */
    String PUBLISH_ARTICLE_QUEUE = "publish.article.queue";
    /**
     * 通往发布文章队列的路由key
     */
    String PUBLISH_ARTICLE_ROUTE_KEY = "delay.publish.article";
    /**
     * 延时队列交换机
     */
    String DELAY_DIRECT_EXCHANGE = "delay.direct";
}
