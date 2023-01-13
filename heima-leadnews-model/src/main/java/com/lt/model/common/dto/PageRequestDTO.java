package com.lt.model.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 分页查询参数
 * @author: ~Teng~
 * @date: 2023/1/13 17:12
 */
@Data
@Slf4j
public class PageRequestDTO {
    @ApiModelProperty(value = "每页显示条数", required = true)
    protected Integer size;
    @ApiModelProperty(value = "当前页", required = true)
    protected Integer page;

    public void checkParam() {
        if (this.page == null || this.page <= 0) {
            setPage(1);
        }
        if (this.size == null || this.size <= 0 || this.size > 100) {
            setSize(10);
        }
    }
}
