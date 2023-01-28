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
 * @date: 2023/1/28 19:40
 */
@Data
@ApiModel("点赞和取消点赞DTO")
@NotNull(message = "传输实体不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
public class LikesBehaviorDTO {
    @ApiModelProperty("设备id")
    private Integer equipmentId;
    @ApiModelProperty("文章id")
    @NotNull(message = "文章id不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Min(value = 1, message = "关联文章id错误", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Long articleId;
    @ApiModelProperty("点赞内容类型 0-文章 1-动态 2-评论")
    @NotNull(message = "点赞内容类型不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Range(min = 0, max = 2, message = "内容类型不符合")
    private Short type;
    @ApiModelProperty("喜欢的操作方式 0-点赞 1-取消点赞")
    @NotNull(message = "操作类型不能为空", groups = {ValidatorUpdateGroup.class, ValidatorAddGroup.class})
    @Range(min = 0, max = 1, message = "操作类型不符合")
    private Short operation;
}
