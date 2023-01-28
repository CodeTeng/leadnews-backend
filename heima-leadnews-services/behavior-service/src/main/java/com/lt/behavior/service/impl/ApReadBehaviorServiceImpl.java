package com.lt.behavior.service.impl;

import com.lt.behavior.service.ApBehaviorEntryService;
import com.lt.behavior.service.ApReadBehaviorService;
import com.lt.model.behavior.dto.ReadBehaviorDTO;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.behavior.pojo.ApReadBehavior;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 21:09
 */
@Service
@Slf4j
public class ApReadBehaviorServiceImpl implements ApReadBehaviorService {
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult readBehavior(ReadBehaviorDTO readBehaviorDTO) {
        // 1. 根据登录用户id 或者设备id 查询行为实体（阅读操作可不用登录）
        Integer equipmentId = readBehaviorDTO.getEquipmentId();
        ApUser apUser = AppThreadLocalUtils.getUser();
        ApBehaviorEntry apBehaviorEntry = null;
        if (apUser == null) {
            // 未登录 游客
            apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(null, equipmentId);
        } else {
            // 登录用户
            apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(apUser.getId(), equipmentId);
        }
        if (apBehaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户实体不存在，无法操作");
        }
        log.info("行为实体：{}", apBehaviorEntry);
        // 2. 判断阅读行为是否存在
        String entryId = apBehaviorEntry.getId();
        Long articleId = readBehaviorDTO.getArticleId();
        Query query = Query.query(Criteria.where("entryId").is(entryId).and("articleId").is(articleId));
        ApReadBehavior apReadBehavior = mongoTemplate.findOne(query, ApReadBehavior.class);
        if (apReadBehavior != null) {
            // 3. 存在 将阅读行为的 count 字段加1 并修改
            apReadBehavior.setCount((short) (apReadBehavior.getCount() + 1));
            apReadBehavior.setUpdatedTime(new Date());
            ApReadBehavior save = mongoTemplate.save(apReadBehavior);
            log.info("更新阅读后的阅读实体：{}", save);
        } else {
            // 4. 不存在 创建阅读实体 并初始化count值为1
            apReadBehavior = new ApReadBehavior();
            apReadBehavior.setEntryId(entryId);
            apReadBehavior.setArticleId(articleId);
            apReadBehavior.setCount((short) 1);
            apReadBehavior.setCreatedTime(new Date());
            apReadBehavior.setUpdatedTime(new Date());
            ApReadBehavior insert = mongoTemplate.insert(apReadBehavior);
            log.info("首次添加阅读后的阅读实体：{}", insert);
        }
        return ResponseResult.okResult();
    }
}
