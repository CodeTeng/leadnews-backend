package com.lt.model.wemedia.dto;

import com.lt.model.common.dto.PageRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/20 16:43
 */
@Data
public class NewsAuthDTO extends PageRequestDTO {
    @ApiModelProperty("文章标题")
    private String title;
    @ApiModelProperty("文章状态")
    private Short status;
    @ApiModelProperty("文章id")
    private Integer id;
    @ApiModelProperty("失败原因")
    private String msg;
}
