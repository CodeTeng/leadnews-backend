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
 * @date: 2023/1/25 15:13
 */
@Data
@TableName("ap_article_config")
@ApiModel("APP已发布文章配置实体")
public class ApArticleConfig {
    @TableId(value = "id",type = IdType.ID_WORKER)
    @ApiModelProperty("主键id")
    private Long id;
    @TableField("article_id")
    @ApiModelProperty("文章id")
    private Long articleId;
    @TableField("is_comment")
    @ApiModelProperty("是否可评论 1-可以 0-不可以")
    private Boolean isComment;
    @TableField("is_forward")
    @ApiModelProperty("是否可转发 1-可以 0-不可以")
    private Boolean isForward;
    @TableField("is_down")
    @ApiModelProperty("是否下架 1-下架 0-没有下架")
    private Boolean isDown;
    @TableField("is_delete")
    @ApiModelProperty("是否已删除 1-删除 0-未删除")
    private Boolean isDelete;
}
