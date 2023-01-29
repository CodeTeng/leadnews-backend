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
 * @date: 2023/1/29 15:07
 */
@Data
@ApiModel("评论点赞DTO")
public class CommentLikeDTO {
    @ApiModelProperty("评论id")
    @NotBlank(message = "评论id不能为空", groups = {ValidatorUpdateGroup.class})
    private String commentId;
    @ApiModelProperty("0-点赞 1-取消点赞")
    @NotNull(message = "操作类型错误", groups = {ValidatorUpdateGroup.class})
    @Range(min = 0, max = 1, message = "操作类型错误", groups = {ValidatorUpdateGroup.class})
    private Short operation;
}
