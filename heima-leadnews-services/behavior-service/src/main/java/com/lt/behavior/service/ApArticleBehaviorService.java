package com.lt.behavior.service;

import com.lt.model.behavior.dto.ArticleBehaviorDTO;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 22:38
 */
public interface ApArticleBehaviorService {
    /**
     * 加载文章详情 数据回显
     */
    ResponseResult loadArticleBehavior(ArticleBehaviorDTO articleBehaviorDTO);
}
