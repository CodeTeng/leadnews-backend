package com.lt.datasync.service.impl;

import com.google.common.collect.Lists;
import com.lt.common.constants.search.SearchConstants;
import com.lt.datasync.mapper.ApArticleMapper;
import com.lt.datasync.service.EsDataService;
import com.lt.es.service.EsService;
import com.lt.model.article.pojo.ApArticle;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.vo.SearchArticleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 16:54
 */
@Service
public class EsDataServiceImpl implements EsDataService {
    @Autowired
    private EsService<SearchArticleVO> esService;
    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public ResponseResult dataInit() {
        // 1. 判断索引库是否存在
//        if (esService.existIndex(SearchConstants.ARTICLE_INDEX_NAME)) {
//            // 存在 删除索引库
//            esService.deleteIndex(SearchConstants.ARTICLE_INDEX_NAME);
//        }
//        // 2. 创建索引库
//        esService.createIndex(SearchConstants.ARTICLE_INDEX_MAPPING, SearchConstants.ARTICLE_INDEX_NAME);
        // 3. 获取 mysql 数据 并转换 Es 格式
        List<ApArticle> apArticles = apArticleMapper.findAllArticles();
        if (apArticles == null || apArticles.size() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "数据库文章信息不存在");
        }
        List<SearchArticleVO> searchArticleVOList = apArticles.stream().map(apArticle -> {
            SearchArticleVO searchArticleVO = new SearchArticleVO();
            BeanUtils.copyProperties(apArticle, searchArticleVO);
            return searchArticleVO;
        }).collect(Collectors.toList());
        // 4. 分批量插入 es 中
        Lists.partition(searchArticleVOList, 2000).forEach(list -> esService.saveBatch(list, SearchConstants.ARTICLE_INDEX_NAME));
        return ResponseResult.okResult();
    }
}
