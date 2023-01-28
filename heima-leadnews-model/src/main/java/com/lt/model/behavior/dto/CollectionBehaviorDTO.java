package com.lt.model.behavior.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.lt.model.common.validator.ValidatorAddGroup;
import com.lt.model.common.validator.ValidatorUpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 22:10
 */
@Data
@ApiModel("收藏和取消收藏DTO")
public class CollectionBehaviorDTO {
    @ApiModelProperty("设备id")
    private Integer equipmentId;
    @ApiModelProperty("文章id")
    @JsonAlias("entryId") // 前端变量命名entryId 实际为articleId 因此起个别名
    @NotNull(message = "文章id不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Min(value = 1, message = "关联文章id错误", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Long articleId;
    @ApiModelProperty("收藏内容类型 0-文章 1-动态")
    private Short type;
    @ApiModelProperty("操作类型 0-收藏 1-取消收藏")
    @NotNull(message = "操作类型不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Range(min = 0, max = 1, message = "操作类型数据错误", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Short operation;
}
