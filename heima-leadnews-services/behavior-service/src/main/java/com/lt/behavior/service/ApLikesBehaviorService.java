package com.lt.behavior.service;

import com.lt.model.behavior.dto.LikesBehaviorDTO;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 19:46
 */
public interface ApLikesBehaviorService {
    /**
     * 点赞或取消点赞
     */
    ResponseResult likeOrLikeBehavior(LikesBehaviorDTO likesBehaviorDTO);
}
