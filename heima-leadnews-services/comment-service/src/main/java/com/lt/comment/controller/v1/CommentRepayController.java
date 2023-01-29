package com.lt.comment.controller.v1;

import com.lt.comment.service.CommentRepayService;
import com.lt.model.comment.dto.CommentRepayDTO;
import com.lt.model.comment.dto.CommentRepayLikeDTO;
import com.lt.model.comment.dto.CommentRepaySaveDTO;
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

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:15
 */
@RestController
@RequestMapping("/api/v1/comment_repay")
@Api(value = "App回复评论模块", tags = "App回复评论")
public class CommentRepayController {
    @Autowired
    private CommentRepayService commentRepayService;

    @PostMapping("/save")
    @ApiOperation("发表回复评论")
    public ResponseResult saveComment(@RequestBody @Validated(ValidatorUpdateGroup.class) CommentRepaySaveDTO commentRepaySaveDTO) {
        return commentRepayService.saveCommentRepay(commentRepaySaveDTO);
    }

    @PostMapping("/like")
    @ApiOperation("保存回复评论点赞")
    public ResponseResult likeComment(@RequestBody @Validated(ValidatorUpdateGroup.class) CommentRepayLikeDTO commentRepayLikeDTO) {
        return commentRepayService.saveCommentRepayLike(commentRepayLikeDTO);
    }

    @PostMapping("/load")
    @ApiOperation("加载回复评论列表")
    public ResponseResult loadComment(@RequestBody @Validated(ValidatorUpdateGroup.class) CommentRepayDTO commentRepayDTO) {
        Integer size = commentRepayDTO.getSize();
        if (size == null || size <= 0) {
            commentRepayDTO.setSize(10);
        }
        Date minDate = commentRepayDTO.getMinDate();
        if (minDate == null) {
            commentRepayDTO.setMinDate(new Date());
        }
        return commentRepayService.loadCommentRepay(commentRepayDTO);
    }
}
