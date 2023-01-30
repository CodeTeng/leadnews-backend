package com.lt.article.service;

import com.lt.model.mess.app.AggBehaviorDTO;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/30 13:46
 */
public interface HotArticleService {
    /**
     * 计算热点文章
     */
    void computeHotArticle();

    /**
     * 根据文章聚合数据 修改文章热度
     *
     * @param aggBehaviorDTO 聚合文章数据
     */
    void updateApArticleHot(AggBehaviorDTO aggBehaviorDTO);
}
