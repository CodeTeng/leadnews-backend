package com.lt.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.article.dto.ArticleHomeDTO;
import com.lt.model.article.pojo.ApArticle;
import com.lt.model.common.vo.ResponseResult;

public interface ApArticleService extends IService<ApArticle> {
    /**
     * 发布文章
     *
     * @param newsId 文章id
     */
    void publishArticle(Integer newsId);

    /**
     * 根据参数加载文章列表
     *
     * @param loadtype 0为加载更多  1为加载最新
     */
    ResponseResult load(Short loadtype, ArticleHomeDTO dto);
}