package com.lt.user.service;

import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.dto.UserRelationDTO;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 21:30
 */
public interface ApUserRelationService {
    /**
     * 用户关注/取消关注
     */
    ResponseResult follow(UserRelationDTO dto);
}
