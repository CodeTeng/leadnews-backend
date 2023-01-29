package com.lt.model.comment.dto;

import com.lt.model.common.validator.ValidatorUpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:12
 */
@Data
@ApiModel("点赞回复DTO")
public class CommentRepayLikeDTO {
    @ApiModelProperty("回复id")
    @NotBlank(message = "回复id不能为空", groups = {ValidatorUpdateGroup.class})
    private String commentRepayId;
    @ApiModelProperty("0-点赞 1-取消点赞")
    @NotNull(message = "操作类型错误", groups = {ValidatorUpdateGroup.class})
    @Range(min = 0, max = 1, groups = {ValidatorUpdateGroup.class})
    private Short operation;
}
