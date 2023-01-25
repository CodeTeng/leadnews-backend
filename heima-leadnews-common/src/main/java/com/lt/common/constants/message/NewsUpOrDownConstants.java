package com.lt.common.constants.message;

/**
 * @description: 文章上下架相关常量
 * @author: ~Teng~
 * @date: 2023/1/25 18:35
 */
public interface NewsUpOrDownConstants {
    String NEWS_UP_OR_DOWN_EXCHANGE = "wm.news.up.or.down.topic";

    String NEWS_UP_FOR_ARTICLE_CONFIG_QUEUE = "news.up.for.article.config.queue";
    String NEWS_UP_FOR_ES_QUEUE = "news.up.for.es.queue";

    String NEWS_DOWN_FOR_ES_QUEUE = "news.down.for.es.queue";
    String NEWS_DOWN_FOR_ARTICLE_CONFIG_QUEUE = "news.down.for.article.config.queue";

    String NEWS_UP_ROUTE_KEY = "news.up";
    String NEWS_DOWN_ROUTE_KEY = "news.down";
}
