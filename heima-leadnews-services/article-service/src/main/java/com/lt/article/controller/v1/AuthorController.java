package com.lt.article.controller.v1;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lt.article.service.AuthorService;
import com.lt.model.article.pojo.ApAuthor;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 14:57
 */
@Api(value = "app作者管理", tags = "app作者管理Controller")
@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @ApiOperation(value = "查询作者", notes = "根据appUserId查询关联作者信息")
    @GetMapping("/findByUserId/{userId}")
    public ResponseResult findByUserId(@PathVariable("userId") Integer userId) {
        if (userId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return ResponseResult.okResult(authorService.getOne(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getUserId, userId)));
    }

    @ApiOperation(value = "保存作者", notes = "保存作者信息")
    @PostMapping("/save")
    public ResponseResult save(@RequestBody ApAuthor apAuthor) {
        if (apAuthor == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        authorService.save(apAuthor);
        return ResponseResult.okResult();
    }
}
