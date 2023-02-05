package com.lt.search.service;

import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.dto.UserSearchDTO;
import com.lt.model.search.vo.SearchArticleVO;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 17:49
 */
public interface ArticleSearchService {
    /**
     * ES文章分页搜索
     */
    ResponseResult search(UserSearchDTO userSearchDto);

    /**
     * 添加索引文章
     */
    void saveArticle(SearchArticleVO article);

    /**
     * 删除索引文章
     */
    void deleteArticle(String articleId);
}
