package com.lt.model.admin.dto.channel;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/13 21:30
 */
@Data
@ApiModel("添加频道DTO")
public class AddChannelDTO {
    @ApiModelProperty(value = "频道名称", required = true)
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
