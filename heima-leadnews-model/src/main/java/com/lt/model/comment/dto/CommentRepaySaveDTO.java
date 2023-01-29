package com.lt.model.comment.dto;

import com.lt.model.common.validator.ValidatorUpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:10
 */
@Data
@ApiModel("保存回复DTO")
public class CommentRepaySaveDTO {
    @ApiModelProperty("评论id")
    @NotBlank(message = "评论id不能为空", groups = {ValidatorUpdateGroup.class})
    private String commentId;
    @ApiModelProperty("回复内容")
    @NotBlank(message = "回复内容不能为空", groups = {ValidatorUpdateGroup.class})
    @Length(max = 140, message = "回复最多为140个字符", groups = {ValidatorUpdateGroup.class})
    private String content;
}
