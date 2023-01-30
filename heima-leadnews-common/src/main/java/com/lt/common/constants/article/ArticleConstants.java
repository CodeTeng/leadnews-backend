package com.lt.common.constants.article;

/**
 * @description: app 加载标签常量
 * @author: ~Teng~
 * @date: 2023/1/26 18:21
 */
public interface ArticleConstants {
    /**
     * 加载更多
     */
    Short LOADTYPE_LOAD_MORE = 0;
    /**
     * 加载最新
     */
    Short LOADTYPE_LOAD_NEW = 1;
    /**
     * 加载所有
     */
    String DEFAULT_TAG = "__all__";
    /**
     * 文章行为分值
     */
    Integer HOT_ARTICLE_VIEW_WEIGHT = 1;
    Integer HOT_ARTICLE_LIKE_WEIGHT = 3;
    Integer HOT_ARTICLE_COMMENT_WEIGHT = 5;
    Integer HOT_ARTICLE_COLLECTION_WEIGHT = 8;
    /**
     * 存到redis热文章前缀
     */
    String HOT_ARTICLE_FIRST_PAGE = "hot_article_first_page_";
}
