package com.lt.model.wemedia.pojo;

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
 * @date: 2023/1/18 22:46
 */
@Data
@TableName("wm_material")
@ApiModel("自媒体图文素材实体")
public class WmMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("user_id")
    @ApiModelProperty("自媒体用户ID")
    private Integer userId;

    @TableField("url")
    @ApiModelProperty("图片地址")
    private String url;

    @TableField("type")
    @ApiModelProperty("素材类型 0-图片 1-视频")
    private Short type;

    @TableField("is_collection")
    @ApiModelProperty("是否收藏 0-不 1-是")
    private Short isCollection;

    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;
}
