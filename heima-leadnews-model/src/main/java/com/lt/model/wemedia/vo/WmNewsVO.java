package com.lt.model.wemedia.vo;

import com.lt.model.wemedia.pojo.WmNews;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/20 16:40
 */
@Data
public class WmNewsVO extends WmNews {
    @ApiModelProperty("作者名称")
    private String authorName;
}
