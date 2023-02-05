package com.lt.model.search.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 17:47
 */
@Data
@ApiModel("搜索文章DTO")
public class UserSearchDTO {
    @ApiModelProperty("设备ID")
    private Integer equipmentId;
    @ApiModelProperty("搜索关键字")
    private String searchWords;
    @ApiModelProperty("当前页")
    private int pageNum;
    @ApiModelProperty("分页条数")
    private int pageSize;
    @ApiModelProperty("行为实体id")
    private Integer entryId;
    @ApiModelProperty("最小时间")
    private Date minBehotTime;

    /**
     * 获取当前页索引
     */
    public int getFromIndex() {
        if (this.pageNum < 1) {
            return 0;
        }
        if (this.pageSize < 1) {
            this.pageSize = 10;
        }
        return this.pageSize * (pageNum - 1);
    }
}
