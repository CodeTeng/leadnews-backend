package com.lt.model.article.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 18:29
 */
@Data
@ApiModel("app文章加载DTO")
public class ArticleHomeDTO {
    @ApiModelProperty("最大时间")
    private Date maxBehotTime;
    @ApiModelProperty("最小时间")
    private Date minBehotTime;
    @ApiModelProperty("分页size")
    private Integer size;
    @ApiModelProperty("频道id")
    private String tag;
}
