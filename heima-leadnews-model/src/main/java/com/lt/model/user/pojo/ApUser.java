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
 * @date: 2023/1/15 15:08
 */
@Data
@TableName("ap_user")
@ApiModel("APP用户信息实体")
public class ApUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("salt")
    @ApiModelProperty("密码、通信等加密盐")
    private String salt;

    @TableField("name")
    @ApiModelProperty("用户名")
    private String name;

    @TableField("password")
    @ApiModelProperty("密码,md5加密")
    private String password;

    @TableField("phone")
    @ApiModelProperty("手机号")
    private String phone;

    @TableField("image")
    @ApiModelProperty("头像")
    private String image;

    @TableField("sex")
    @ApiModelProperty("0-男 1-女 2-未知")
    private Boolean sex;

    @TableField("is_certification")
    @ApiModelProperty("0-未 1-是")
    private Boolean certification;

    @TableField("is_identity_authentication")
    @ApiModelProperty("是否身份认证 0-否 1-是")
    private Boolean identityAuthentication;

    @TableField("status")
    @ApiModelProperty("0-正常 1-锁定")
    private Boolean status;

    @TableField("flag")
    @ApiModelProperty("0-普通用户 1-自媒体人 2-大V")
    private Short flag;

    @TableField("created_time")
    @ApiModelProperty("注册时间")
    private Date createdTime;
}
