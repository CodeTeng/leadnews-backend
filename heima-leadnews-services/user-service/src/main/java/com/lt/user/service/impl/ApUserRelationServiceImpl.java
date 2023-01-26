package com.lt.user.service.impl;

import com.lt.common.constants.user.UserRelationConstants;
import com.lt.exception.CustomException;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.dto.UserRelationDTO;
import com.lt.model.user.pojo.ApUser;
import com.lt.user.service.ApUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 21:31
 */
@Service
public class ApUserRelationServiceImpl implements ApUserRelationService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseResult follow(UserRelationDTO dto) {
        // 1. 校验参数
        // 1.1 必须登录才能关注或取关
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "需要登录才能关注或取关");
        }
        // 1.2 对应作者是否存在
        Integer authorApUserId = dto.getAuthorApUserId();
        if (authorApUserId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "作者对应的userId不存在");
        }
        // 1.3 操作方式是否正确
        Short operation = dto.getOperation();
        if (operation == null || (operation.intValue() != 0 && operation.intValue() != 1)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "关注类型错误");
        }
        // 1.4 不能自己关注自己
        Integer loginId = apUser.getId();
        if (authorApUserId.equals(loginId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "不可以自己关注自己哦~");
        }
        // 1.5 不能重复关注
        Double score = stringRedisTemplate.opsForZSet()
                .score(UserRelationConstants.FOLLOW_LIST + loginId, String.valueOf(authorApUserId));
        if (operation.intValue() == 0 && score != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "您已关注，无法重复关注");
        }
        try {
            // 2. 进行关注或取关
            if (operation.intValue() == 0) {
                // 关注
                // 将对方写入我的关注中
                stringRedisTemplate.opsForZSet()
                        .add(UserRelationConstants.FOLLOW_LIST + loginId,
                                String.valueOf(authorApUserId),
                                System.currentTimeMillis());
                // 将我写入对方的粉丝中
                stringRedisTemplate.opsForZSet()
                        .add(UserRelationConstants.FANS_LIST + authorApUserId,
                                String.valueOf(loginId),
                                System.currentTimeMillis());
            } else {
                // 取关
                stringRedisTemplate.opsForZSet().remove(UserRelationConstants.FOLLOW_LIST + loginId, String.valueOf(authorApUserId));
                stringRedisTemplate.opsForZSet().remove(UserRelationConstants.FANS_LIST + authorApUserId, String.valueOf(loginId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR, e.getMessage());
        }
        return ResponseResult.okResult();
    }
}
