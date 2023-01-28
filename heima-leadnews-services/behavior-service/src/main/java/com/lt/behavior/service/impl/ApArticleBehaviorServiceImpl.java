package com.lt.behavior.service.impl;

import com.lt.behavior.service.ApArticleBehaviorService;
import com.lt.behavior.service.ApBehaviorEntryService;
import com.lt.common.constants.user.UserRelationConstants;
import com.lt.model.behavior.dto.ArticleBehaviorDTO;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.behavior.pojo.ApCollection;
import com.lt.model.behavior.pojo.ApLikesBehavior;
import com.lt.model.behavior.pojo.ApUnlikesBehavior;
import com.lt.model.behavior.vo.ArticleBehaviorVO;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 22:39
 */
@Service
@Slf4j
public class ApArticleBehaviorServiceImpl implements ApArticleBehaviorService {
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseResult loadArticleBehavior(ArticleBehaviorDTO articleBehaviorDTO) {
        // 1. 判断用户是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        ArticleBehaviorVO articleBehaviorVO = new ArticleBehaviorVO(false, false, false, false);
        if (apUser == null) {
            return ResponseResult.okResult(articleBehaviorVO);
        }
        Integer userId = apUser.getId();
        if (userId == 0) {
            return ResponseResult.okResult(articleBehaviorVO);
        }
        Integer equipmentId = articleBehaviorDTO.getEquipmentId();
        // 2. 查询行为实体
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(userId, equipmentId);
        if (apBehaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户实体不存在，无法操作");
        }
        if (!apBehaviorEntry.isUser()) {
            return ResponseResult.okResult(articleBehaviorVO);
        }
        // 3. 根据文章id查询是否点赞
        String entryId = apBehaviorEntry.getId();
        Long articleId = articleBehaviorDTO.getArticleId();
        Query query = Query.query(Criteria.where("entryId").is(entryId).and("articleId").is(articleId));
        ApLikesBehavior apLikesBehavior = mongoTemplate.findOne(query, ApLikesBehavior.class);
        if (apLikesBehavior == null) {
            // 没有点赞过
            articleBehaviorVO.setIslike(false);
        } else {
            articleBehaviorVO.setIslike(true);
        }
        // 4. 根据文章id查询是否不喜欢
        ApUnlikesBehavior apUnlikesBehavior = mongoTemplate.findOne(query, ApUnlikesBehavior.class);
        if (apUnlikesBehavior == null) {
            // 没有不喜欢
            articleBehaviorVO.setIsunlike(false);
        } else {
            articleBehaviorVO.setIsunlike(true);
        }
        // 5. 根据文章id查询是否收藏
        ApCollection apCollection = mongoTemplate.findOne(query, ApCollection.class);
        if (apCollection == null) {
            // 没有收藏
            articleBehaviorVO.setIscollection(false);
        } else {
            articleBehaviorVO.setIscollection(true);
        }
        // 6. 根据用户id去redis中查询是否关注该作者
        Integer authorApUserId = articleBehaviorDTO.getAuthorApUserId();
        Double score = stringRedisTemplate.opsForZSet().score(UserRelationConstants.FOLLOW_LIST + userId, String.valueOf(authorApUserId));
        if (score == null) {
            // 没有关注
            articleBehaviorVO.setIsfollow(false);
        } else {
            articleBehaviorVO.setIsfollow(true);
        }
        return ResponseResult.okResult(articleBehaviorVO);
    }
}
