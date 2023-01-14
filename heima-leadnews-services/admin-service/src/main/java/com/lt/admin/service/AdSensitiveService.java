package com.lt.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.admin.dto.sensitive.SensitiveDTO;
import com.lt.model.admin.pojo.AdSensitive;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 16:02
 */
public interface AdSensitiveService extends IService<AdSensitive> {
    /**
     * 根据名称模糊分页查询
     *
     * @param sensitiveDTO 条件分页参数
     */
    ResponseResult getSensitiveByNameAndPage(SensitiveDTO sensitiveDTO);
}
