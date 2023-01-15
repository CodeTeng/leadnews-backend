package com.lt.admin.controller.v1;

import com.lt.admin.service.AdUserService;
import com.lt.model.admin.dto.aduser.LoginAdUserDTO;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
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
 * @date: 2023/1/14 20:47
 */
@RestController
@RequestMapping("/login")
@Api(value = "运营平台登录管理", tags = "运营平台登录Controller")
public class LoginController {
    @Autowired
    private AdUserService userService;

    @PostMapping("/in")
    @ApiOperation("用户登录")
    public ResponseResult login(@RequestBody LoginAdUserDTO loginAdUserDTO) {
        if (loginAdUserDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        String name = loginAdUserDTO.getName();
        String password = loginAdUserDTO.getPassword();
        if (StringUtils.isAnyBlank(name, password)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return userService.login(loginAdUserDTO);
    }

}
