package com.lt.comment.controller.v1;

import com.lt.comment.service.CommentService;
import com.lt.model.comment.dto.CommentDTO;
import com.lt.model.comment.dto.CommentLikeDTO;
import com.lt.model.comment.dto.CommentSaveDTO;
import com.lt.model.common.validator.ValidatorAddGroup;
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
 * @date: 2023/1/29 14:46
 */
@RestController
@RequestMapping("/api/v1/comment")
@Api(value = "评论模块管理", tags = "评论模块Controller")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    @ApiOperation("发表评论")
    public ResponseResult saveComment(@RequestBody @Validated(ValidatorAddGroup.class) CommentSaveDTO commentSaveDTO) {
        return commentService.saveComment(commentSaveDTO);
    }

    @PostMapping("/like")
    @ApiOperation("点赞评论")
    public ResponseResult likeComment(@RequestBody @Validated(ValidatorUpdateGroup.class) CommentLikeDTO commentLikeDTO) {
        return commentService.likeComment(commentLikeDTO);
    }

    @PostMapping("/load")
    @ApiOperation("加载评论列表")
    public ResponseResult loadComment(@RequestBody @Validated(ValidatorUpdateGroup.class) CommentDTO commentDTO) {
        Short index = commentDTO.getIndex();
        if (index <= 0) {
            // 是否第一页 等于1代表第一页，热点评论时使用
            commentDTO.setIndex((short) 1);
        }
        Integer size = commentDTO.getSize();
        if (size == null || size <= 0) {
            commentDTO.setSize(10);
        }
        Date minDate = commentDTO.getMinDate();
        if (minDate == null) {
            commentDTO.setMinDate(new Date());
        }
        return commentService.loadComment(commentDTO);
    }
}
