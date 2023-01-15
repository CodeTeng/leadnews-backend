package com.lt.user.controller.v1;

import com.lt.common.constants.admin.AdminConstants;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.dto.AuthDTO;
import com.lt.user.service.ApUserRealnameService;
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
 * @date: 2023/1/15 11:42
 */
@Api(value = "app用户实名认证管理", tags = "app用户实名认证Controller")
@RestController
@RequestMapping("/api/v1/auth")
public class ApUserRealnameController {
    @Autowired
    private ApUserRealnameService userRealnameService;

    @ApiOperation("根据状态查询实名认证列表")
    @PostMapping("/list")
    public ResponseResult loadListByStatus(@RequestBody AuthDTO authDTO) {
        if (authDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        authDTO.checkParam();
        return userRealnameService.getApUserListByStatus(authDTO);
    }

    @ApiOperation("实名认证通过")
    @PostMapping("/authPass")
    public ResponseResult authPass(@RequestBody AuthDTO authDTO) {
        if (authDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Integer id = authDTO.getId();
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "实名认证ID不能为空");
        }
        authDTO.checkParam();
        return userRealnameService.updateStatusById(authDTO, AdminConstants.PASS_AUTH);
    }

    @ApiOperation("实名认证失败")
    @PostMapping("/authFail")
    public ResponseResult authFail(@RequestBody AuthDTO authDTO) {
        if (authDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Integer id = authDTO.getId();
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "实名认证ID不能为空");
        }
        authDTO.checkParam();
        return userRealnameService.updateStatusById(authDTO, AdminConstants.FAIL_AUTH);
    }
}
