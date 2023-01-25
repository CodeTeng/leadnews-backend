package com.lt.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.article.pojo.ApArticle;

public interface ApArticleService extends IService<ApArticle> {
    /**
     * 发布文章
     *
     * @param newsId 文章id
     */
    void publishArticle(Integer newsId);
}