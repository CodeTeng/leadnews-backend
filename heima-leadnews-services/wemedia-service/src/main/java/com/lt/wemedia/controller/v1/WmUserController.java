package com.lt.wemedia.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.pojo.WmUser;
import com.lt.wemedia.service.WmUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 13:57
 */
@Api(value = "自媒体用户管理", tags = "自媒体用户Controller")
@RestController
@RequestMapping("/api/v1/user")
public class WmUserController {
    @Autowired
    private WmUserService wmUserService;

    @GetMapping("/findByName/{name}")
    @ApiOperation("根据用户名查询自媒体用户信息")
    public ResponseResult findByName(@PathVariable("name") String name) {
        if (StringUtils.isBlank(name)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmUser wmUser = wmUserService.getOne(new LambdaQueryWrapper<WmUser>().eq(WmUser::getName, name));
        return ResponseResult.okResult(wmUser);
    }

    @PostMapping("/save")
    @ApiOperation("保存自媒体用户信息")
    public ResponseResult save(@RequestBody WmUser wmUser) {
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        wmUserService.save(wmUser);
        return ResponseResult.okResult(wmUser);
    }
}
