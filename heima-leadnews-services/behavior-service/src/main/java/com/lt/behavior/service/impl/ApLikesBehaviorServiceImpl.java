package com.lt.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.lt.common.constants.article.HotArticleConstants;
import com.lt.model.mess.app.NewBehaviorDTO.BehaviorType;

import com.lt.behavior.service.ApBehaviorEntryService;
import com.lt.behavior.service.ApLikesBehaviorService;
import com.lt.model.behavior.dto.LikesBehaviorDTO;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.behavior.pojo.ApLikesBehavior;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.mess.app.NewBehaviorDTO;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 19:57
 */
@Service
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseResult likeOrLikeBehavior(LikesBehaviorDTO likesBehaviorDTO) {
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
        Integer equipmentId = likesBehaviorDTO.getEquipmentId();
        ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(userId, equipmentId);
        if (behaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户实体不存在，无法操作");
        }
        if (!behaviorEntry.isUser()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        String entryId = behaviorEntry.getId();
        Short operation = likesBehaviorDTO.getOperation();
        Long articleId = likesBehaviorDTO.getArticleId();
        Short type = likesBehaviorDTO.getType();
        // 3. 根据类型进行点赞或取消点赞
        if (operation.intValue() == 1) {
            // 取消点赞
            mongoTemplate.remove(
                    Query.query(Criteria.where("entryId").is(entryId).and("articleId").is(articleId).and("type").is(type)),
                    ApLikesBehavior.class);
            // 发送取消点赞消息
            NewBehaviorDTO newBehaviorDTO = new NewBehaviorDTO();
            newBehaviorDTO.setType(BehaviorType.LIKES);
            newBehaviorDTO.setArticleId(articleId);
            newBehaviorDTO.setAdd(-1);
            rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE, JSON.toJSONString(newBehaviorDTO));
            log.info("发送取消点赞行为消息成功：{}", newBehaviorDTO);
            return ResponseResult.okResult("取消点赞成功");
        } else {
            // 点赞
            // 3.1 判断是否已经点过赞
            ApLikesBehavior likesBehavior = mongoTemplate.findOne(
                    Query.query(Criteria.where("entryId").is(entryId).and("articleId").is(articleId).and("type").is(type)),
                    ApLikesBehavior.class);
            if (likesBehavior != null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "已经点赞过该文章，无法再次点赞");
            }
            // 3.2 可以点赞
            ApLikesBehavior apLikesBehavior = new ApLikesBehavior();
            BeanUtils.copyProperties(likesBehaviorDTO, apLikesBehavior);
            apLikesBehavior.setEntryId(entryId);
            apLikesBehavior.setCreatedTime(new Date());
            ApLikesBehavior insert = mongoTemplate.insert(apLikesBehavior);
            log.info("点赞实体：{}", insert);
            // 发送点赞消息
            NewBehaviorDTO newBehaviorDTO = new NewBehaviorDTO();
            newBehaviorDTO.setType(BehaviorType.LIKES);
            newBehaviorDTO.setArticleId(articleId);
            newBehaviorDTO.setAdd(1);
            rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE, JSON.toJSONString(newBehaviorDTO));
            log.info("发送点赞行为消息成功：{}", newBehaviorDTO);
            return ResponseResult.okResult("点赞成功");
        }
    }
}
