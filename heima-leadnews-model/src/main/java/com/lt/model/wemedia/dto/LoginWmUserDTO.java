package com.lt.model.wemedia.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 21:07
 */
@Data
@ApiModel("自媒体登录请求实体")
public class LoginWmUserDTO {
    @ApiModelProperty(value = "用户名", required = true)
    private String name;

    @ApiModelProperty(value = "密码", required = true)
    private String password;
}
