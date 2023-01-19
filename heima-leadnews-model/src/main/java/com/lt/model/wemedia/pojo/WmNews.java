package com.lt.model.wemedia.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 11:59
 */
@Data
@TableName("wm_news")
@ApiModel("自媒体图文内容实体")
public class WmNews implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("user_id")
    @ApiModelProperty("自媒体用户ID")
    private Integer userId;

    @TableField("title")
    @ApiModelProperty("标题")
    private String title;

    @TableField("content")
    @ApiModelProperty("图文内容")
    private String content;

    @TableField("type")
    @ApiModelProperty("文章布局 0-无图文章 1-单图文章 3-多图文章")
    private Short type;

    @TableField("channel_id")
    @ApiModelProperty("图文频道ID")
    private Integer channelId;

    @TableField("labels")
    @ApiModelProperty("文章标签")
    private String labels;

    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @TableField("submited_time")
    @ApiModelProperty("提交时间")
    private Date submitedTime;

    @TableField("status")
    @ApiModelProperty("当前状态 0-草稿 1-提交(待审核) 2-审核失败 3-人工审核 4-人工审核通过 8-审核通过(待发布) 9-已发布")
    private Short status;

    @TableField("publish_time")
    @ApiModelProperty("定时发布时间 不定时则为空")
    private Date publishTime;

    @TableField("reason")
    @ApiModelProperty("拒绝理由")
    private String reason;

    @TableField("article_id")
    @ApiModelProperty("发布库文章id")
    private Long articleId;

    @TableField("images")
    @ApiModelProperty("图片，多个用,分隔")
    private String images;

    @TableField("enable")
    @ApiModelProperty("是否上架")
    private Short enable;

    /**
     * 枚举状态类
     */
    @Alias("WmNewsStatus")
    public enum Status {
        /**
         * 自媒体图文状态
         */
        NORMAL((short) 0), SUBMIT((short) 1), FAIL((short) 2), ADMIN_AUTH((short) 3), ADMIN_SUCCESS((short) 4), SUCCESS((short) 8), PUBLISHED((short) 9);
        short code;

        Status(short code) {
            this.code = code;
        }

        public short getCode() {
            return this.code;
        }
    }
}
