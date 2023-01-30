package com.lt.model.article.vo;

import com.lt.model.article.pojo.ApArticle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/30 13:47
 */
@Data
public class HotArticleVo extends ApArticle {
    @ApiModelProperty("分值")
    private Integer score;
}
