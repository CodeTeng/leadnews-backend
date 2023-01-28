package com.lt.behavior.controller.v1;

import com.lt.behavior.service.*;
import com.lt.model.behavior.dto.*;
import com.lt.model.common.validator.ValidatorUpdateGroup;
import com.lt.model.common.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 22:40
 */
@RestController
@RequestMapping("/api/v1")
@Api(value = "行为模块管理", tags = "行为模块Controller")
public class ApBehaviorController {
    @Autowired
    ApLikesBehaviorService apLikesBehaviorService;
    @Autowired
    ApReadBehaviorService apReadBehaviorService;
    @Autowired
    ApUnlikeBehaviorService apUnlikeBehaviorService;
    @Autowired
    ApCollectionBehaviorService apCollectionBehaviorService;
    @Autowired
    ApArticleBehaviorService apArticleBehaviorService;

    @PostMapping("/likes_behavior")
    @ApiOperation("保存或取消点赞行为")
    public ResponseResult likesBehavior(@RequestBody @Validated(ValidatorUpdateGroup.class) LikesBehaviorDTO likesBehaviorDTO) {
        return apLikesBehaviorService.likeOrLikeBehavior(likesBehaviorDTO);
    }

    @PostMapping("/read_behavior")
    @ApiOperation("保存阅读行为")
    public ResponseResult readBehavior(@RequestBody @Validated(ValidatorUpdateGroup.class) ReadBehaviorDTO readBehaviorDTO) {
        return apReadBehaviorService.readBehavior(readBehaviorDTO);
    }

    @PostMapping("/un_likes_behavior")
    @ApiOperation("保存或取消不喜欢行为")
    public ResponseResult unLikesBehavior(@RequestBody @Validated(ValidatorUpdateGroup.class) UnLikesBehaviorDTO unLikesBehaviorDTO) {
        return apUnlikeBehaviorService.unLikeBehavior(unLikesBehaviorDTO);
    }

    @PostMapping("/collection_behavior")
    @ApiOperation("保存或取消收藏行为")
    public ResponseResult unLikesBehavior(@RequestBody @Validated(ValidatorUpdateGroup.class) CollectionBehaviorDTO collectionBehaviorDTO) {
        return apCollectionBehaviorService.collectBehavior(collectionBehaviorDTO);
    }

    @PostMapping("/article/load_article_behavior")
    @ApiOperation("查询文章行为相关信息")
    public ResponseResult loadArticleBehavior(@RequestBody @Validated(ValidatorUpdateGroup.class) ArticleBehaviorDTO articleBehaviorDTO) {
        return apArticleBehaviorService.loadArticleBehavior(articleBehaviorDTO);
    }
}
