package com.lt.search.controller.v1;

import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.dto.UserSearchDTO;
import com.lt.search.service.ArticleSearchService;
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
 * @date: 2023/2/4 17:46
 */
@Api(value = "文章搜索API", tags = "文章搜索Controller")
@RestController
@RequestMapping("/api/v1/article/search")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    @ApiOperation("文章搜索")
    @PostMapping("/search")
    public ResponseResult search(@RequestBody UserSearchDTO userSearchDto) {
        return articleSearchService.search(userSearchDto);
    }
}
