package com.lt.comment.service;

import com.lt.model.comment.dto.CommentRepayDTO;
import com.lt.model.comment.dto.CommentRepayLikeDTO;
import com.lt.model.comment.dto.CommentRepaySaveDTO;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:13
 */
public interface CommentRepayService {
    /**
     * 查看更多回复内容
     */
    ResponseResult loadCommentRepay(CommentRepayDTO dto);

    /**
     * 保存回复
     */
    ResponseResult saveCommentRepay(CommentRepaySaveDTO dto);

    /**
     * 点赞回复的评论
     */
    ResponseResult saveCommentRepayLike(CommentRepayLikeDTO dto);
}
