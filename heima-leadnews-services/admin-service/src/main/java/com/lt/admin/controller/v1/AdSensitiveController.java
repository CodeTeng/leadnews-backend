package com.lt.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lt.admin.service.AdSensitiveService;
import com.lt.model.admin.dto.sensitive.SensitiveDTO;
import com.lt.model.admin.pojo.AdSensitive;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 16:00
 */
@RestController
@RequestMapping("/api/v1/sensitive")
@Api(value = "敏感词管理", tags = "敏感词管理Controller")
public class AdSensitiveController {
    @Autowired
    private AdSensitiveService sensitiveService;

    @PostMapping("/list")
    @ApiOperation(value = "敏感词分页列表查询", notes = "条件：名称模糊查询")
    public ResponseResult findByNameAndPage(@RequestBody SensitiveDTO sensitiveDTO) {
        if (sensitiveDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 检查分页参数
        sensitiveDTO.checkParam();
        return sensitiveService.getSensitiveByNameAndPage(sensitiveDTO);
    }

    @ApiOperation(value = "添加敏感词", notes = "敏感词不能为空 不能重复添加")
    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult save(@RequestBody AdSensitive adSensitive) {
        if (adSensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        String name = adSensitive.getSensitives();
        if (StringUtils.isBlank(name)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 判断频道是否存在
        LambdaQueryWrapper<AdSensitive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdSensitive::getSensitives, name);
        int count = sensitiveService.count(queryWrapper);
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADMIN_SENSITIVE_EXIST);
        }
        // 执行新增
        boolean flag = sensitiveService.save(adSensitive);
        if (flag) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.OPERATION_CHANNEL_INSERT_ERROR);
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新敏感词")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult update(@RequestBody AdSensitive adSensitive) {
        if (adSensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 获取原始频道
        AdSensitive sensitive = sensitiveService.getById(adSensitive.getId());
        if (sensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADMIN_SENSITIVE_NOT_EXIST);
        }
        if (StringUtils.isBlank(adSensitive.getSensitives())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        } else {
            // 校验唯一性
            if (!sensitive.getSensitives().equals(adSensitive.getSensitives())) {
                int count = sensitiveService.count(new QueryWrapper<AdSensitive>().eq("sensitives", adSensitive.getSensitives()));
                if (count > 0) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.ADMIN_SENSITIVE_EXIST);
                }
            }
        }
        // 执行更新
        boolean flag = sensitiveService.updateById(adSensitive);
        if (flag) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.OPERATION_CHANNEL_UPDATE_ERROR);
        }
    }

    @DeleteMapping("/del/{id}")
    @ApiOperation(value = "删除敏感词")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteById(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        AdSensitive sensitive = sensitiveService.getById(id);
        if (sensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADMIN_SENSITIVE_NOT_EXIST);
        }
        // 删除频道
        boolean flag = sensitiveService.removeById(id);
        if (flag) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.OPERATION_CHANNEL_DELETE_ERROR);
        }
    }
}
