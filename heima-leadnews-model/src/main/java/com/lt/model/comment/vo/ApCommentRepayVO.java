package com.lt.model.comment.vo;

import com.lt.model.comment.pojo.ApCommentRepay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:14
 */
@Data
@ApiModel("回复评论列表VO")
public class ApCommentRepayVO extends ApCommentRepay {
    @ApiModelProperty("0-点赞 1-取消点赞")
    private Short operation;
}
