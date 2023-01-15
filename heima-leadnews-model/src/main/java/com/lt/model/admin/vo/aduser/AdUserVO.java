package com.lt.model.admin.vo.aduser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 20:42
 */
@Data
@ApiModel("运营平台登录用户VO")
public class AdUserVO {
    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String image;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("最后登录时间")
    private Date loginTime;

    @ApiModelProperty("创建时间")
    private Date createdTime;
}
