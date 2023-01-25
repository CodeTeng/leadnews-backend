package com.lt.model.article.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/25 15:23
 */
@Data
@TableName("ap_article_content")
@ApiModel("文章内容实体")
public class ApArticleContent {
    @TableId(value = "id", type = IdType.ID_WORKER)
    @ApiModelProperty("主键id")
    private Long id;
    @TableField("article_id")
    @ApiModelProperty("文章id")
    private Long articleId;
    @ApiModelProperty("文章内容")
    private String content;
}
