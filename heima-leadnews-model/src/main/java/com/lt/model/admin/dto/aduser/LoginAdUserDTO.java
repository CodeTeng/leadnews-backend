package com.lt.model.admin.dto.aduser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 20:38
 */
@ApiModel("运营平台用户登录DTO")
@Data
public class LoginAdUserDTO {
    @ApiModelProperty(value = "用户名", required = true)
    private String name;

    @ApiModelProperty(value = "密码", required = true)
    private String password;
}
