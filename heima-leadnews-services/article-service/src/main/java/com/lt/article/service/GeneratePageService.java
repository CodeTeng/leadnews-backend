package com.lt.article.service;

import com.lt.model.article.pojo.ApArticle;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 11:39
 */
public interface GeneratePageService {
    /**
     * 基于 freemarker 生成文章静态页
     */
    void generateArticlePage(String content, ApArticle apArticle);
}
