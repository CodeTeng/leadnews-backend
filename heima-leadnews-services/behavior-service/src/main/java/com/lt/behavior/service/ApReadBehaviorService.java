package com.lt.behavior.service;

import com.lt.model.behavior.dto.ReadBehaviorDTO;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 21:08
 */
public interface ApReadBehaviorService {
    /**
     * 记录阅读行为
     */
    ResponseResult readBehavior(ReadBehaviorDTO readBehaviorDTO);
}
