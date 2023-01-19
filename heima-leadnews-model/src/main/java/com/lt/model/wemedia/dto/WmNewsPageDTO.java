package com.lt.model.wemedia.dto;

import com.lt.model.common.dto.PageRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 12:13
 */
@Data
public class WmNewsPageDTO extends PageRequestDTO {
    @ApiModelProperty("文章状态")
    private Short status;
    @ApiModelProperty("开始时间")
    private Date beginPubDate;
    @ApiModelProperty("结束时间")
    private Date endPubDate;
    @ApiModelProperty("所属频道ID")
    private Integer channelId;
    @ApiModelProperty("文章名称")
    private String keyword;
}
