package com.lt.model.admin.dto;

import com.lt.model.common.dto.PageRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 请求分页频道DTO
 * @author: ~Teng~
 * @date: 2023/1/13 19:20
 */
@Data
public class ChannelDTO extends PageRequestDTO {
    @ApiModelProperty("频道名称")
    private String name;

    @ApiModelProperty("频道状态 1-开启 0-关闭")
    private Integer status;
}
