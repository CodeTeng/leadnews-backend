package com.lt.search.service;

import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.dto.HistorySearchDTO;
import com.lt.model.search.dto.UserSearchDTO;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 13:32
 */
public interface ApUserSearchService {
    /**
     * 保存用户搜索历史记录
     */
    void insert(UserSearchDTO userSearchDto);

    /**
     * 查询搜索历史
     */
    ResponseResult findUserSearch(UserSearchDTO userSearchDto);

    /**
     * 删除搜索历史
     */
    ResponseResult delUserSearch(HistorySearchDTO historySearchDto);
}
