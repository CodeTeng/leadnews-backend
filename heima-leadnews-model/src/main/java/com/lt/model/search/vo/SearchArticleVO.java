package com.lt.model.search.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lt.model.common.anno.EsId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 15:41
 */
@Data
@ApiModel("搜索文章VO")
public class SearchArticleVO {
    @EsId
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("文章id")
    private Long id;
    @ApiModelProperty(" 文章标题")
    private String title;
    @ApiModelProperty("文章发布时间")
    private Date publishTime;
    @ApiModelProperty("文章布局")
    private Integer layout;
    @ApiModelProperty("封面")
    private String images;
    @ApiModelProperty("作者")
    private Long authorId;
    @ApiModelProperty("作者名字")
    private String authorName;
    @ApiModelProperty("静态url")
    private String staticUrl;
}
