package com.lt.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.admin.mapper.AdChannelMapper;
import com.lt.admin.service.AdChannelService;
import com.lt.model.admin.dto.channel.ChannelDTO;
import com.lt.model.admin.pojo.AdChannel;
import com.lt.model.common.vo.PageResponseResult;
import com.lt.model.common.vo.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/13 19:25
 */
@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {

    @Override
    public ResponseResult getChannelByNameAndPage(ChannelDTO channelDTO) {
        LambdaQueryWrapper<AdChannel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNoneBlank(channelDTO.getName()), AdChannel::getName, channelDTO.getName());
        queryWrapper.eq(channelDTO.getStatus() != null, AdChannel::getStatus, channelDTO.getStatus());
        // 序号升序
        queryWrapper.orderByAsc(AdChannel::getOrd);
        // 进行分页
        Page<AdChannel> page = new Page<>(channelDTO.getPage(), channelDTO.getSize());
        IPage<AdChannel> pageResult = this.page(page, queryWrapper);
        // 封装响应结果
        PageResponseResult pageResponseResult = new PageResponseResult(channelDTO.getPage(), channelDTO.getSize(), pageResult.getTotal(), pageResult.getRecords());
        return pageResponseResult;
    }
}
