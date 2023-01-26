package com.lt.model.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 17:38
 */
@Data
@ApiModel("app登录DTO")
public class ApUserLoginDTO {
    @ApiModelProperty("设备id")
    private Integer equipmentId;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("密码")
    private String password;
}
