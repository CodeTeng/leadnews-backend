package com.lt.model.user.pojo;

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
 * @date: 2023/1/15 11:26
 */
@Data
@TableName("ap_user_realname")
@ApiModel("APP实名认证信息实体")
public class ApUserRealname implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("user_id")
    @ApiModelProperty("账号id")
    private Integer userId;

    @TableField("name")
    @ApiModelProperty("用户名称")
    private String name;

    @TableField("idno")
    @ApiModelProperty("资源名称")
    private String idno;

    @TableField("font_image")
    @ApiModelProperty("正面照片")
    private String fontImage;

    @TableField("back_image")
    @ApiModelProperty("背面照片")
    private String backImage;

    @TableField("hold_image")
    @ApiModelProperty("手持照片")
    private String holdImage;

    @TableField("live_image")
    @ApiModelProperty("活体照片")
    private String liveImage;

    @TableField("status")
    @ApiModelProperty("状态 0-创建中 1-待审核 2-审核失败 3-审核通过")
    private Short status;

    @TableField("reason")
    @ApiModelProperty("拒绝原因")
    private String reason;

    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @TableField("submited_time")
    @ApiModelProperty("提交时间")
    private Date submitedTime;

    @TableField("updated_time")
    @ApiModelProperty("更新时间")
    private Date updatedTime;
}
