package com.lt.comment.service;

import com.lt.model.comment.pojo.ApComment;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 18:21
 */
public interface CommentHotService {
    /**
     * 查找热点评论
     */
    void hotCommentExecutor(ApComment apComment);
}
