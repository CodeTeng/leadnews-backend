package com.lt.model.wemedia.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 16:12
 */
@Data
public class DownOrUpNewsDTO {
    @ApiModelProperty("文章id")
    private Integer id;
    @ApiModelProperty("是否上架")
    private Short enable;
}
