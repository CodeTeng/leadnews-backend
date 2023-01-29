package com.lt.comment.service;

import com.lt.model.comment.dto.CommentDTO;
import com.lt.model.comment.dto.CommentLikeDTO;
import com.lt.model.comment.dto.CommentSaveDTO;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 14:07
 */
public interface CommentService {
    /**
     * 发表评论
     */
    ResponseResult saveComment(CommentSaveDTO commentSaveDTO);

    /**
     * 点赞评论
     */
    ResponseResult likeComment(CommentLikeDTO commentLikeDTO);

    /**
     * 根据文章id查询评论列表
     */
    ResponseResult loadComment(CommentDTO commentDTO);
}
