package com.lt.model.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 分页查询返回对象
 * @author: ~Teng~
 * @date: 2023/1/13 17:16
 */
@Data
public class PageResponseResult extends ResponseResult implements Serializable {
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 一页显示记录
     */
    private Integer size;
    /**
     * 总记录
     */
    private Long total;

    public PageResponseResult() {
    }

    public PageResponseResult(Integer currentPage, Integer size, Long total) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
    }

    public PageResponseResult(Integer currentPage, Integer size, Long total, Object data) {
        super.setData(data);
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
    }
}
