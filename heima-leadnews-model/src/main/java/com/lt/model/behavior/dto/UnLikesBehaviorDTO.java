package com.lt.model.behavior.dto;

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
 * @date: 2023/1/28 21:43
 */
@Data
@ApiModel("不喜欢实体DTO")
public class UnLikesBehaviorDTO {
    @ApiModelProperty("设备id")
    private Integer equipmentId;
    @ApiModelProperty("文章id")
    @NotNull(message = "文章id不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Min(value = 1, message = "关联文章id错误", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Long articleId;
    @ApiModelProperty("不喜欢操作方式 0-不喜欢 1-取消不喜欢")
    @NotNull(message = "操作方式类型错误", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Range(min = 0, max = 1, message = "操作方式错误", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Short type;
}
