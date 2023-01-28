package com.lt.behavior.service;

import com.lt.model.behavior.dto.UnLikesBehaviorDTO;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 21:45
 */
public interface ApUnlikeBehaviorService {
    /**
     * 保存 或 取消 不喜欢
     */
    ResponseResult unLikeBehavior(UnLikesBehaviorDTO unLikesBehaviorDTO);
}
