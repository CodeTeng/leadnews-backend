package com.lt.model.comment.dto;

import com.lt.model.common.validator.ValidatorAddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 14:01
 */
@Data
@ApiModel("发表评论DTO")
public class CommentSaveDTO {
    @ApiModelProperty("文章id")
    @NotNull(message = "文章id不能为空", groups = {ValidatorAddGroup.class})
    private Long articleId;
    @ApiModelProperty("评论内容")
    @NotBlank(message = "评论内容不能为空", groups = {ValidatorAddGroup.class})
    @Length(max = 140, message = "评论内容长度不能超过140字符长度", groups = {ValidatorAddGroup.class})
    private String content;
}