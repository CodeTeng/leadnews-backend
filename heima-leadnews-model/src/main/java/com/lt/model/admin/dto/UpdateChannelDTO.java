package com.lt.model.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/13 21:52
 */
@Data
@ApiModel("修改频道DTO")
public class UpdateChannelDTO {
    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("频道名称")
    private String name;

    @ApiModelProperty("频道描述")
    private String description;

    @ApiModelProperty("是否默认频道")
    private Boolean isDefault;

    @ApiModelProperty("状态")
    private Boolean status;

    @ApiModelProperty("默认排序")
    private Integer ord;
}
