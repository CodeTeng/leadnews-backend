package com.lt.model.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 21:27
 */
@Data
@ApiModel("关注取关DTO")
public class UserRelationDTO {
    @ApiModelProperty("文章作者id")
    private Integer authorId;
    @ApiModelProperty("作者对应的apuserid")
    private Integer authorApUserId;
    @ApiModelProperty("文章id")
    private Long articleId;
    @ApiModelProperty("操作方式 0-关注 1-取关")
    private Short operation;
}
