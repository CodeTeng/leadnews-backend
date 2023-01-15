package com.lt.model.article.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 14:53
 */
@Data
@TableName("ap_author")
@ApiModel("APP文章作者信息实体")
public class ApAuthor implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("name")
    @ApiModelProperty("作者名称")
    private String name;

    @TableField("type")
    @ApiModelProperty("类型 0-爬取数据 1-签约合作商 2-平台自媒体人")
    private Integer type;

    @TableField("user_id")
    @ApiModelProperty("APP用户ID")
    private Integer userId;

    @TableField("wm_user_id")
    @ApiModelProperty("自媒体账号id")
    private Integer wmUserId;

    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;
}
