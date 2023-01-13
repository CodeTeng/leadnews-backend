package com.lt.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lt.admin.service.AdChannelService;
import com.lt.model.admin.dto.AddChannelDTO;
import com.lt.model.admin.dto.ChannelDTO;
import com.lt.model.admin.dto.UpdateChannelDTO;
import com.lt.model.admin.pojo.AdChannel;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 频道管理
 * @author: ~Teng~
 * @date: 2023/1/13 19:26
 */
@RestController
@RequestMapping("/api/v1/channel")
@Api(value = "频道管理", tags = "频道管理Controller", description = "频道管理API")
public class AdChannelController {
    @Autowired
    private AdChannelService channelService;

    @PostMapping("/list")
    @ApiOperation(value = "频道分页列表查询", notes = "条件：频道名称、状态，以ord升序查询频道")
    public ResponseResult findByNameAndPage(@RequestBody ChannelDTO channelDTO) {
        if (channelDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 检查分页参数
        channelDTO.checkParam();
        return channelService.getChannelByNameAndPage(channelDTO);
    }

    @ApiOperation(value = "添加频道", notes = "频道名称不能为空且不能大于10个字符，频道名不能重复")
    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult save(@RequestBody AddChannelDTO addChannelDTO) {
        if (addChannelDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        String name = addChannelDTO.getName();
        if (StringUtils.isBlank(name)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        if (name.length() > 10) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 判断频道是否存在
        LambdaQueryWrapper<AdChannel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdChannel::getName, name);
        int count = channelService.count(queryWrapper);
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADMING_CHANNEL_EXIST);
        }
        // 执行新增
        AdChannel adChannel = new AdChannel();
        BeanUtils.copyProperties(addChannelDTO, adChannel);
        boolean flag = channelService.save(adChannel);
        if (flag) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.OPERATION_CHANNEL_INSERT_ERROR);
        }
    }

    @PostMapping("/api/v1/channel/update")
    @ApiOperation(value = "更新频道", notes = "如果有name需处理，无则不处理")
    public ResponseResult update(@RequestBody UpdateChannelDTO updateChannelDTO) {
        if (updateChannelDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 获取原始频道
        AdChannel channel = channelService.getById(updateChannelDTO.getId());
        if (channel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADMING_CHANNEL_NOT_EXIST);
        }
        if (StringUtils.isBlank(updateChannelDTO.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        } else {
            // 校验唯一性
            if (!channel.getName().equals(updateChannelDTO.getName())) {
                int count = channelService.count(new LambdaQueryWrapper<AdChannel>().eq(AdChannel::getName, updateChannelDTO.getName()));
                if (count > 0) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.ADMING_CHANNEL_EXIST);
                }
            }
        }
        // 执行更新
        BeanUtils.copyProperties(updateChannelDTO, channel);
        boolean flag = channelService.updateById(channel);
        if (flag) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.OPERATION_CHANNEL_UPDATE_ERROR);
        }
    }

    @GetMapping("/api/v1/channel/del/{id}")
    @ApiOperation(value = "删除频道")
    public ResponseResult deleteById(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        AdChannel channel = channelService.getById(id);
        if (channel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADMING_CHANNEL_NOT_EXIST);
        }
        // 频道处于正常状态 无法删除
        if (channel.getStatus()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADMING_CHANNEL_STATUS_TRUE);
        }
        // 删除频道
        boolean flag = channelService.removeById(id);
        if (flag) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.OPERATION_CHANNEL_DELETE_ERROR);
        }
    }
}
