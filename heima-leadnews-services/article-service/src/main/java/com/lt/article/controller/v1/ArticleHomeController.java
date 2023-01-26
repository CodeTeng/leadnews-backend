package com.lt.article.controller.v1;

import com.lt.article.service.ApArticleService;
import com.lt.common.constants.article.ArticleConstants;
import com.lt.model.article.dto.ArticleHomeDTO;
import com.lt.model.common.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 19:00
 */
@Api(tags = "app端首页文章列表接口", value = "app端首页文章列表接口Controller")
@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {
    @Autowired
    private ApArticleService articleService;

    @ApiOperation(value = "查询热点文章列表", notes = "第一次点击各类频道时，调用此接口 查询该频道对应热点文章")
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDTO dto) {
        return articleService.load(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
    }

    @ApiOperation(value = "查询更多文章", notes = "app文章列表下拉时 根据页面文章最小时间 实现滚动查询")
    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDTO dto) {
        return articleService.load(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
    }

    @ApiOperation(value = "查询最新文章", notes = "app文章列表上拉时 根据页面文章最大时间 查询最新文章")
    @PostMapping("/loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDTO dto) {
        return articleService.load(ArticleConstants.LOADTYPE_LOAD_NEW, dto);
    }
}
