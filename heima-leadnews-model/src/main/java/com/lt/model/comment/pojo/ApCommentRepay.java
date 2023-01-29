package com.lt.model.comment.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:04
 */
@Data
@Document("ap_comment_repay")
@ApiModel("app评论回复信息")
public class ApCommentRepay {
    @Id
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("当前登录用户id")
    private Integer authorId;
    @ApiModelProperty("用户昵称")
    private String authorName;
    @ApiModelProperty("评论id")
    private String commentId;
    @ApiModelProperty("回复内容")
    private String content;
    @ApiModelProperty("点赞数")
    private Integer likes;
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    @ApiModelProperty("维度")
    private BigDecimal latitude;
    @ApiModelProperty("地理位置")
    private String address;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("更新时间")
    private Date updatedTime;
}
