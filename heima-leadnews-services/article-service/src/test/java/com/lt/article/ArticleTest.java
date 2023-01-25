package com.lt.article;

import com.lt.article.service.ApArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/25 16:49
 */
@SpringBootTest
public class ArticleTest {
    @Autowired
    ApArticleService apArticleService;

    @Test
    public void publishArticle() {
        apArticleService.publishArticle(6271);
    }
}
