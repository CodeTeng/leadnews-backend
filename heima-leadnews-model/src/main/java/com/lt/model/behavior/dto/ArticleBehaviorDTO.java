package com.lt.model.behavior.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lt.model.common.validator.ValidatorAddGroup;
import com.lt.model.common.validator.ValidatorUpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 22:35
 */
@Data
@ApiModel("文章行为DTO")
public class ArticleBehaviorDTO {
    @ApiModelProperty("设备id")
    private Integer equipmentId;
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("文章id")
    @JsonAlias("entryId") // 前端变量命名entryId 实际为articleId 因此起个别名
    @NotNull(message = "文章id不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Min(value = 1, message = "关联文章id错误", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Long articleId;
    @ApiModelProperty("作者ID")
    @NotNull(message = "作者id不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Integer authorId;
    @ApiModelProperty("作者对应的apuserid")
    @NotNull(message = "作者对应的apuserid不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Integer authorApUserId;
}
