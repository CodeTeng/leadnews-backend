package com.lt.model.user.dto;

import com.lt.model.common.dto.PageRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 11:38
 */
@Data
public class AuthDTO extends PageRequestDTO {
    @ApiModelProperty("状态")
    private Short status;

    @ApiModelProperty("认证用户信息ID")
    private Integer id;

    @ApiModelProperty("驳回的信息")
    private String msg;
}
