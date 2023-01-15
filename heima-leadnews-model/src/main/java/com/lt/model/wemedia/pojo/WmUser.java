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
 * @date: 2023/1/15 13:51
 */
@Data
@TableName("wm_user")
@ApiModel("自媒体用户信息实体")
public class WmUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("ap_user_id")
    @ApiModelProperty("APPUser Id")
    private Integer apUserId;

    @TableField("name")
    @ApiModelProperty("登录用户名")
    private String name;

    @TableField("password")
    @ApiModelProperty("登录密码")
    private String password;

    @TableField("salt")
    @ApiModelProperty("盐")
    private String salt;

    @TableField("nickname")
    @ApiModelProperty("昵称")
    private String nickname;

    @TableField("image")
    @ApiModelProperty("头像")
    private String image;

    @TableField("location")
    @ApiModelProperty("归属地")
    private String location;

    @TableField("phone")
    @ApiModelProperty("手机号")
    private String phone;

    @TableField("status")
    @ApiModelProperty("状态 0-暂时不可用 1-永久不可用 9-正常可用")
    private Integer status;

    @TableField("email")
    @ApiModelProperty("邮箱")
    private String email;

    @TableField("type")
    @ApiModelProperty("账号类型 0-个人 1-企业 2-子账号")
    private Integer type;

    @TableField("score")
    @ApiModelProperty("运营评分")
    private Integer score;

    @TableField("login_time")
    @ApiModelProperty("最后一次登录时间")
    private Date loginTime;

    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;
}
