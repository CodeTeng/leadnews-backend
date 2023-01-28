package com.lt.behavior.service.impl;

import com.lt.behavior.service.ApBehaviorEntryService;
import com.lt.behavior.service.ApUnlikeBehaviorService;
import com.lt.model.behavior.dto.UnLikesBehaviorDTO;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.behavior.pojo.ApUnlikesBehavior;
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
 * @date: 2023/1/28 21:46
 */
@Service
@Slf4j
public class ApUnlikeBehaviorServiceImpl implements ApUnlikeBehaviorService {
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult unLikeBehavior(UnLikesBehaviorDTO unLikesBehaviorDTO) {
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
        Integer equipmentId = unLikesBehaviorDTO.getEquipmentId();
        ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(userId, equipmentId);
        if (behaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户实体不存在，无法操作");
        }
        if (!behaviorEntry.isUser()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        String entryId = behaviorEntry.getId();
        Long articleId = unLikesBehaviorDTO.getArticleId();
        Short type = unLikesBehaviorDTO.getType();
        // 3. 判断是不喜欢 还是取消不喜欢
        Query query = Query.query(Criteria.where("entryId").is(entryId).and("articleId").is(articleId));
        ApUnlikesBehavior apUnlikesBehavior = mongoTemplate.findOne(query, ApUnlikesBehavior.class);
        if (ApUnlikesBehavior.Type.UNLIKE.getCode() == type) {
            // 不喜欢操作
            // 4. 判断不喜欢行为是否存在 不能重复不喜欢
            if (apUnlikesBehavior != null) {
                // 已经存在 不能重复不喜欢
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "不能重复操作不喜欢");
            }
            // 添加不喜欢行为
            apUnlikesBehavior = new ApUnlikesBehavior();
            apUnlikesBehavior.setEntryId(entryId);
            apUnlikesBehavior.setType(type);
            apUnlikesBehavior.setArticleId(articleId);
            apUnlikesBehavior.setCreatedTime(new Date());
            ApUnlikesBehavior insert = mongoTemplate.insert(apUnlikesBehavior);
            log.info("首次添加不喜欢行为：{}", insert);
            return ResponseResult.okResult("添加不喜欢行为成功");
        } else {
            // 4. 取消不喜欢操作
            // 判断是否存在 不存在不能进行该操作
            if (apUnlikesBehavior == null) {
                // 已经存在 不能重复不喜欢
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "没有该数据，无法取消不喜欢");
            }
            mongoTemplate.remove(apUnlikesBehavior);
            return ResponseResult.okResult("取消不喜欢成功");
        }
    }
}
