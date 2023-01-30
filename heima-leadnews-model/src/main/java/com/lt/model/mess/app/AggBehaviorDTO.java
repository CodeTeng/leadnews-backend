package com.lt.model.mess.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/30 16:35
 */
@Data
@ApiModel("行为聚合数据DTO")
public class AggBehaviorDTO {
    @ApiModelProperty("文章id")
    private Long articleId;
    @ApiModelProperty("阅读数量")
    private long view;
    @ApiModelProperty("收藏数量")
    private long collect;
    @ApiModelProperty("评论数量")
    private long comment;
    @ApiModelProperty("点赞数量")
    private long like;
}
