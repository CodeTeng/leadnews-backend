package com.lt.model.wemedia.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 13:12
 */
@Data
@ApiModel("发布文章请求DTO")
public class SubmitWmNewsDTO {
    @ApiModelProperty("文章id")
    private Integer id;

    @ApiModelProperty(value = "文章标题", required = true)
    private String title;

    @ApiModelProperty(value = "频道id", required = true)
    private Integer channelId;

    @ApiModelProperty(value = "文章标签", required = true)
    private String labels;

    @ApiModelProperty("发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "文章内容", required = true)
    private String content;

    @ApiModelProperty(value = "文章封面类型  0-无图 1-单图 3-多图 -1-自动", required = true)
    private Short type;

    @ApiModelProperty("是否上架  0-下架  1-上架")
    private Short enable;

    @ApiModelProperty("提交时间")
    private Date submitedTime;

    @ApiModelProperty(value = "状态 1-提交 0-草稿", required = true)
    private Short status;

    @ApiModelProperty("拒绝理由")
    private String reason;

    @ApiModelProperty("封面图片列表")
    private List<String> images;
}
