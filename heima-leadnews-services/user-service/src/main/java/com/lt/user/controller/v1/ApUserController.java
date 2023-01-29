package com.lt.user.controller.v1;

import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.pojo.ApUser;
import com.lt.user.service.ApUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 14:05
 */
@Api(value = "app端用户管理", tags = "app端用户管理Controller")
@RestController
@RequestMapping("/api/v1/user")
public class ApUserController {
    @Autowired
    private ApUserService apUserService;

    @ApiOperation("根据id查询apUser信息")
    @GetMapping("/{id}")
    public ResponseResult findUserById(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser apUser = apUserService.getById(id);
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "app用户不存在");
        }
        return ResponseResult.okResult(apUser);
    }
}
