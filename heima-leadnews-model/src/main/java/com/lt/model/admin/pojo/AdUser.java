package com.lt.model.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 管理员用户信息表
 *
 * @author teng
 * @TableName ad_user
 */
@TableName(value = "ad_user")
@Data
@ApiModel("用户实体")
public class AdUser implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("name")
    @ApiModelProperty("用户名")
    private String name;

    @TableField("password")
    @ApiModelProperty("密码")
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

    @TableField("phone")
    @ApiModelProperty("手机号")
    private String phone;

    @TableField("status")
    @ApiModelProperty("状态 0-暂时不可用 1-永久不可用 9-正常使用")
    private Integer status;

    @TableField("email")
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("最后登录时间")
    @TableField("login_time")
    private Date loginTime;

    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}