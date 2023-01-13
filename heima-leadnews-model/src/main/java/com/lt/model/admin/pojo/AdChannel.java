package com.lt.model.admin.pojo;

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
 * @description: 频道信息实体
 * @author: ~Teng~
 * @date: 2023/1/13 19:02
 */
@Data
@TableName("ad_channel")
@ApiModel("频道信息")
public class AdChannel implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("name")
    @ApiModelProperty("频道名称")
    private String name;

    @TableField("description")
    @ApiModelProperty("频道描述")
    private String description;

    @TableField("is_default")
    @ApiModelProperty("是否默认频道")
    private Boolean isDefault;

    @TableField("status")
    @ApiModelProperty("状态")
    private Boolean status;

    @TableField("ord")
    @ApiModelProperty("默认排序")
    private Integer ord;

    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;
}
