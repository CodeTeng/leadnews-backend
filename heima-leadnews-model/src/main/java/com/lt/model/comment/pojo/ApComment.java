package com.lt.model.comment.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 13:51
 */
@Data
@Document("ap_comment")
@ApiModel("app评论实体")
public class ApComment {
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("当前登陆人id")
    private Integer authorId;
    @ApiModelProperty("用户昵称")
    private String authorName;
    @ApiModelProperty("文章id或动态id")
    private Long articleId;
    @ApiModelProperty("评论内容类型 0-文章 1-动态")
    private Short type;
    @ApiModelProperty("频道id")
    private Integer channelId;
    @ApiModelProperty("品论内容 不能超过140个字")
    private String content;
    @ApiModelProperty("登陆人头像")
    private String image;
    @ApiModelProperty("点赞数")
    private Integer likes;
    @ApiModelProperty("回复数")
    private Integer reply;
    @ApiModelProperty("文章标记 0-普通评论 1-热点评论 2-推荐评论 3-指定评论 4-精品评论 5-大V评论")
    private Short flag;
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    @ApiModelProperty("维度")
    private BigDecimal latitude;
    @ApiModelProperty("地理位置")
    private String address;
    @ApiModelProperty("评论排列序号")
    private Integer ord;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("更新时间")
    private Date updatedTime;
}
