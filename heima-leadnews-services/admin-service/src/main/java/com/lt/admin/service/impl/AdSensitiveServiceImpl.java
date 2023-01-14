package com.lt.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.admin.mapper.AdSensitiveMapper;
import com.lt.admin.service.AdSensitiveService;
import com.lt.model.admin.dto.sensitive.SensitiveDTO;
import com.lt.model.admin.pojo.AdSensitive;
import com.lt.model.common.vo.PageResponseResult;
import com.lt.model.common.vo.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 16:08
 */
@Service
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper, AdSensitive> implements AdSensitiveService {

    @Override
    public ResponseResult getSensitiveByNameAndPage(SensitiveDTO sensitiveDTO) {
        LambdaQueryWrapper<AdSensitive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNoneBlank(sensitiveDTO.getName()), AdSensitive::getSensitives, sensitiveDTO.getName());
        // 进行分页
        Page<AdSensitive> page = new Page<>(sensitiveDTO.getPage(), sensitiveDTO.getSize());
        IPage<AdSensitive> pageResult = this.page(page, queryWrapper);
        // 封装响应结果
        return new PageResponseResult(sensitiveDTO.getPage(), sensitiveDTO.getSize(), pageResult.getTotal(), pageResult.getRecords());
    }
}
