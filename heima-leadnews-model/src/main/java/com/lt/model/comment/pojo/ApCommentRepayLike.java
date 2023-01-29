package com.lt.model.comment.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:07
 */
@Data
@Document("ap_comment_repay_like")
@ApiModel("APP评论回复信息点赞实体")
public class ApCommentRepayLike {
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("用户id")
    private Integer authorId;
    @ApiModelProperty("评论id")
    private String commentRepayId;
    @ApiModelProperty("0-点赞 1-取消点赞")
    private Short operation;
}
