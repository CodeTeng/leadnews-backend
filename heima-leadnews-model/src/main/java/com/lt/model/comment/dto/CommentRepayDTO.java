package com.lt.model.comment.dto;

import com.lt.model.common.validator.ValidatorUpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:09
 */
@Data
@ApiModel("加载评论回复DTO")
public class CommentRepayDTO {
    @ApiModelProperty("评论id")
    @NotBlank(message = "评论id不能为空", groups = {ValidatorUpdateGroup.class})
    private String commentId;
    @ApiModelProperty("当前显示")
    private Integer size;
    @ApiModelProperty("最小时间")
    private Date minDate;
}
