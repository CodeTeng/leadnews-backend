package com.lt.user.controller.v1;

import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.dto.UserRelationDTO;
import com.lt.user.service.ApUserRelationService;
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
 * @date: 2023/1/26 21:51
 */
@Api(value = "用户关注，取关管理", tags = "用户关注，取关Controller")
@RestController
@RequestMapping("/api/v1/user")
public class ApUserRelationController {
    @Autowired
    private ApUserRelationService apUserRelationService;

    @ApiOperation("关注 或 取关")
    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserRelationDTO dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return apUserRelationService.follow(dto);
    }
}
