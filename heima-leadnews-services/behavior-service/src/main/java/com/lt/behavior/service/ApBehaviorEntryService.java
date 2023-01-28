package com.lt.behavior.service;

import com.lt.model.behavior.pojo.ApBehaviorEntry;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 20:29
 */
public interface ApBehaviorEntryService {
    /**
     * 根据用户id 和设备id 查询当前行为实体
     *
     * @param userId      用户id
     * @param equipmentId 设备id
     * @return 行为实体
     */
    ApBehaviorEntry findByUserIdOrEquipmentId(Integer userId, Integer equipmentId);
}
