package com.lt.behavior.service;

import com.lt.model.behavior.dto.CollectionBehaviorDTO;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 22:15
 */
public interface ApCollectionBehaviorService {
    /**
     * 收藏 取消收藏
     */
    ResponseResult collectBehavior(CollectionBehaviorDTO collectionBehaviorDTO);
}
