package com.lt.user.controller.v1;

import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.dto.ApUserLoginDTO;
import com.lt.user.service.ApUserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 17:37
 */
@Api(value = "app端用户登录管理", tags = "app端用户登录Controller")
@RestController
@RequestMapping("/api/v1/login")
public class ApUserLoginController {
    @Autowired
    private ApUserLoginService apUserLoginService;

    @ApiOperation("登录")
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody ApUserLoginDTO apUserLoginDTO) {
        if (apUserLoginDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return apUserLoginService.login(apUserLoginDTO);
    }
}
