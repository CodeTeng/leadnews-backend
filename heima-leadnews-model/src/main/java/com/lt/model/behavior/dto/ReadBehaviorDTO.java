package com.lt.model.behavior.dto;

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
 * @date: 2023/1/28 21:06
 */
@Data
@ApiModel("阅读行为DTO")
public class ReadBehaviorDTO {
    @ApiModelProperty("设备id")
    private Integer equipmentId;
    @ApiModelProperty("文章id")
    @NotNull(message = "文章id不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Min(value = 1, message = "关联文章id错误", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Long articleId;
    @ApiModelProperty("阅读次数")
    private Short count;
}
