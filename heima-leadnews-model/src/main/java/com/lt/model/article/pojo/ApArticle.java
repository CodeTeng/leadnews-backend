package com.lt.model.article.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/25 15:16
 */
@Data
@TableName("ap_article")
@ApiModel("文章信息实体，存储已发布的文章")
public class ApArticle {
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("文章主键id")
    private Long id;
    @ApiModelProperty("文章标题")
    private String title;
    @TableField("author_id")
    @ApiModelProperty("作者id")
    private Long authorId;
    @TableField("author_name")
    @ApiModelProperty("作者名称")
    private String authorName;
    @TableField("channel_id")
    @ApiModelProperty("频道id")
    private Integer channelId;
    @TableField("channel_name")
    @ApiModelProperty("频道名称")
    private String channelName;
    @ApiModelProperty("文章布局 0-无图文章 1-单图文章 2-多图文章")
    private Short layout;
    @ApiModelProperty("文章标题 0-普通文章 1-热点文章 2-置顶文章 3-精品文章 4-大V文章")
    private Byte flag;
    @ApiModelProperty("文章封面图片,多张用,分隔")
    private String images;
    @ApiModelProperty("标签")
    private String labels;
    @ApiModelProperty("点赞数量")
    private Integer likes;
    @ApiModelProperty("收藏数量")
    private Integer collection;
    @ApiModelProperty("评论数量")
    private Integer comment;
    @ApiModelProperty("阅读数量")
    private Integer views;
    @ApiModelProperty("省份id")
    @TableField("province_id")
    private Integer provinceId;
    @ApiModelProperty("市区id")
    @TableField("city_id")
    private Integer cityId;
    @ApiModelProperty("区县id")
    @TableField("county_id")
    private Integer countyId;
    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @TableField("publish_time")
    @ApiModelProperty("发布时间")
    private Date publishTime;
    @TableField("sync_status")
    @ApiModelProperty("同步状态")
    private Boolean syncStatus;
    @ApiModelProperty("来源")
    private Boolean origin;
    @TableField("static_url")
    @ApiModelProperty("文章静态页路径")
    private String staticUrl;
}
