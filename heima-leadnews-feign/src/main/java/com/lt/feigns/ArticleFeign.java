package com.lt.feigns;

import com.lt.config.HeimaFeignAutoConfiguration;
import com.lt.feigns.fallback.ArticleFeignFallback;
import com.lt.model.article.pojo.ApArticle;
import com.lt.model.article.pojo.ApAuthor;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.vo.SearchArticleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 14:58
 */
@FeignClient(value = "leadnews-article",
        fallbackFactory = ArticleFeignFallback.class,
        configuration = HeimaFeignAutoConfiguration.class
)
public interface ArticleFeign {
    @GetMapping("/api/v1/author/findByUserId/{userId}")
    ResponseResult<ApAuthor> findByUserId(@PathVariable("userId") Integer userId);

    @PostMapping("/api/v1/author/save")
    ResponseResult save(@RequestBody ApAuthor apAuthor);

    @GetMapping("/api/v1/article/findById/{id}")
    ResponseResult<ApArticle> findById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/article/{id}")
    ResponseResult<SearchArticleVO> findArticle(@PathVariable("id") Long id);
}
