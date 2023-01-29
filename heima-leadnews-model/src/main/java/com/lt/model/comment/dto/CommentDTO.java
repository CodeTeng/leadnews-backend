package com.lt.model.comment.dto;

import com.lt.model.common.validator.ValidatorAddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 15:33
 */
@Data
@ApiModel("评论列表DTO")
public class CommentDTO {
    @ApiModelProperty("文章id")
    @NotNull(message = "文章id不能为空", groups = {ValidatorAddGroup.class})
    private Long articleId;
    @ApiModelProperty("最小时间")
    private Date minDate;
    @ApiModelProperty("是否是首页")
    private Short index;
    @ApiModelProperty("每页条数")
    private Integer size;
}
