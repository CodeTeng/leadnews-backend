package com.lt.model.comment.vo;

import com.lt.model.comment.pojo.ApComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 15:35
 */
@Data
@ApiModel("评论列表VO")
public class ApCommentVO extends ApComment {
    @ApiModelProperty("0-点赞 1-取消点赞")
    private Short operation;
}
