package com.lt.behavior.service.impl;

import com.lt.behavior.service.ApBehaviorEntryService;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.behavior.pojo.ApLikesBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 20:29
 */
@Service
public class ApBehaviorEntryServiceImpl implements ApBehaviorEntryService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ApBehaviorEntry findByUserIdOrEquipmentId(Integer userId, Integer equipmentId) {
        // 1. 判断 userId 是否为空
        if (userId != null) {
            Query query = Query.query(Criteria.where("refId").is(userId).and("type").is(ApBehaviorEntry.Type.USER.getCode()));
            // 1.1 查询行为实体是否为空
            ApBehaviorEntry behaviorEntry = mongoTemplate.findOne(query, ApBehaviorEntry.class);
            if (behaviorEntry == null) {
                // 为空的话首次创建
                ApBehaviorEntry apBehaviorEntry = new ApBehaviorEntry();
                apBehaviorEntry.setType((short) 1);
                apBehaviorEntry.setRefId(userId);
                apBehaviorEntry = mongoTemplate.insert(apBehaviorEntry);
                return apBehaviorEntry;
            }
            // 不为空直接返回
            return behaviorEntry;
        } else {
            // 2. userId 为空 判断设备id是否为空
            if (equipmentId == null) {
                // 用户和设备id都为空 直接返回 null
                return null;
            }
            // 3. 设备id不为空 游客 未登录状态
            Query query = Query.query(Criteria.where("refId").is(equipmentId).and("type").is(ApBehaviorEntry.Type.EQUIPMENT.getCode()));
            ApBehaviorEntry behaviorEntry = mongoTemplate.findOne(query, ApBehaviorEntry.class);
            if (behaviorEntry == null) {
                // 为空首次创建
                ApBehaviorEntry apBehaviorEntry = new ApBehaviorEntry();
                apBehaviorEntry.setType((short) 0);
                apBehaviorEntry.setRefId(equipmentId);
                apBehaviorEntry = mongoTemplate.insert(apBehaviorEntry);
                return apBehaviorEntry;
            }
            // 不为空直接返回
            return behaviorEntry;
        }
    }
}
