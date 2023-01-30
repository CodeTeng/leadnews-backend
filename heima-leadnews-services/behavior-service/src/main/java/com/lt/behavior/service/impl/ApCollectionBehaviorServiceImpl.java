package com.lt.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.lt.common.constants.article.HotArticleConstants;
import com.lt.model.mess.app.NewBehaviorDTO.BehaviorType;

import com.lt.behavior.service.ApBehaviorEntryService;
import com.lt.behavior.service.ApCollectionBehaviorService;
import com.lt.model.behavior.dto.CollectionBehaviorDTO;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.behavior.pojo.ApCollection;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.mess.app.NewBehaviorDTO;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 22:15
 */
@Service
@Slf4j
public class ApCollectionBehaviorServiceImpl implements ApCollectionBehaviorService {
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseResult collectBehavior(CollectionBehaviorDTO collectionBehaviorDTO) {
        // 1. 判断用户是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        Integer userId = apUser.getId();
        if (userId == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        // 2. 根据当前用户查询行为实体
        Integer equipmentId = collectionBehaviorDTO.getEquipmentId();
        ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(userId, equipmentId);
        if (behaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户实体不存在，无法操作");
        }
        if (!behaviorEntry.isUser()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        // 3. 判断是收藏还是取消收藏
        String entryId = behaviorEntry.getId();
        Short operation = collectionBehaviorDTO.getOperation();
        Long articleId = collectionBehaviorDTO.getArticleId();
        Short type = collectionBehaviorDTO.getType();
        Query query = Query.query(Criteria.where("entryId").is(entryId).and("articleId").is(articleId).and("type").is(type));
        ApCollection apCollection = mongoTemplate.findOne(query, ApCollection.class);
        if (operation == 0) {
            // 收藏
            // 4.1 判断是否收藏过
            if (apCollection != null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "无法再次收藏");
            }
            apCollection = new ApCollection();
            apCollection.setEntryId(entryId);
            apCollection.setArticleId(articleId);
            apCollection.setCollectionTime(new Date());
            apCollection.setType(type);
            ApCollection insert = mongoTemplate.insert(apCollection);
            log.info("首次收藏成功：{}", insert);
            // 发送消息
            NewBehaviorDTO newBehaviorDTO = new NewBehaviorDTO();
            newBehaviorDTO.setType(BehaviorType.COLLECTION);
            newBehaviorDTO.setArticleId(articleId);
            newBehaviorDTO.setAdd(1);
            rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE, JSON.toJSONString(newBehaviorDTO));
            log.info("发送收藏行为消息成功：{}", newBehaviorDTO);
            return ResponseResult.okResult("收藏成功");
        } else {
            // 取消收藏
            if (apCollection == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "文章未收藏，无法取消收藏");
            }
            mongoTemplate.remove(apCollection);
            // 发送消息
            NewBehaviorDTO newBehaviorDTO = new NewBehaviorDTO();
            newBehaviorDTO.setType(BehaviorType.COLLECTION);
            newBehaviorDTO.setArticleId(articleId);
            newBehaviorDTO.setAdd(-1);
            rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE, JSON.toJSONString(newBehaviorDTO));
            log.info("发送取消收藏行为消息成功：{}", newBehaviorDTO);
            return ResponseResult.okResult("取消收藏成功");
        }
    }
}
