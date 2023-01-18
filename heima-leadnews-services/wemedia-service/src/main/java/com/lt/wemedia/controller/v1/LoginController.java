package com.lt.wemedia.controller.v1;

import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.dto.LoginWmUserDTO;
import com.lt.wemedia.service.WmUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 21:10
 */
@RestController
@RequestMapping("/login")
@Api(value = "自媒体登录管理", tags = "自媒体登录Controller")
public class LoginController {
    @Autowired
    private WmUserService wmUserService;

    @ApiOperation("自媒体登录")
    @PostMapping("/in")
    public ResponseResult login(@RequestBody LoginWmUserDTO loginWmUserDTO) {
        if (loginWmUserDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        String name = loginWmUserDTO.getName();
        String password = loginWmUserDTO.getPassword();
        if (StringUtils.isAnyBlank(name, password)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return wmUserService.login(loginWmUserDTO);
    }
}
