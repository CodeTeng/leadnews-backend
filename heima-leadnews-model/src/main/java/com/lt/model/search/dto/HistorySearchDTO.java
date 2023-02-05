package com.lt.model.search.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 14:21
 */
@Data
@ApiModel("搜索记录DTO")
public class HistorySearchDTO {
    @ApiModelProperty("设备ID")
    private Integer equipmentId;
    @ApiModelProperty("接收搜索历史记录id")
    private String id;
}
