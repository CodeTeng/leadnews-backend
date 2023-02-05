package com.lt.search.service.impl;

import com.lt.common.constants.search.SearchConstants;
import com.lt.es.service.EsService;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.PageResponseResult;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.dto.UserSearchDTO;
import com.lt.model.search.vo.SearchArticleVO;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import com.lt.search.service.ApUserSearchService;
import com.lt.search.service.ArticleSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 17:49
 */
@Service
@Slf4j
public class ArticleSearchServiceImpl implements ArticleSearchService {
    @Autowired
    private EsService<SearchArticleVO> esService;
    @Value("${file.minio.readPath}")
    private String readPath;
    @Value("${file.oss.web-site}")
    private String webSite;
    @Autowired
    private ApUserSearchService apUserSearchService;

    @Override
    public ResponseResult search(UserSearchDTO userSearchDto) {
        // 1. 检查参数
        String searchWords = userSearchDto.getSearchWords();
        if (StringUtils.isBlank(searchWords)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "搜索条件内容不能为空");
        }
        Date minBehotTime = userSearchDto.getMinBehotTime();
        if (minBehotTime == null) {
            minBehotTime = new Date();
        }
        // 记录搜索历史记录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser != null) {
            userSearchDto.setEntryId(apUser.getId());
        }
        // 异步调用保存用户输入关键词记录
        apUserSearchService.insert(userSearchDto);
        // 2. 构建 ES 请求
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 关键字 title 查询
        boolQueryBuilder.must(QueryBuilders.matchQuery("title", searchWords));
        // 发布时间小于最小时间
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("publishTime").lt(minBehotTime));
        searchSourceBuilder.query(boolQueryBuilder);
        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder
                .field("title")
                .preTags("<font style='color: red; font-size: inherit;'>")
                .postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 发布时间倒序
        searchSourceBuilder.sort("publishTime", SortOrder.DESC);
        // 分页
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(userSearchDto.getPageSize());
        // 3. 发送请求
        PageResponseResult searchResult = esService.search(searchSourceBuilder, SearchArticleVO.class, SearchConstants.ARTICLE_INDEX_NAME);
        // 4. 返回结果 staticUrl 需要拼接 minIO文章服务器的前缀路径
        List<SearchArticleVO> list = (List<SearchArticleVO>) searchResult.getData();
        if (CollectionUtils.isNotEmpty(list)) {
            for (SearchArticleVO searchArticleVo : list) {
                searchArticleVo.setStaticUrl(readPath + searchArticleVo.getStaticUrl());
                String images = searchArticleVo.getImages();
                if (StringUtils.isNotBlank(images)) {
                    images = Arrays.stream(images.split(","))
                            .map(url -> webSite + url).collect(Collectors.joining(","));
                }
                searchArticleVo.setImages(images);
            }
        }
        return searchResult;
    }

    @Override
    public void saveArticle(SearchArticleVO article) {
        esService.save(article, SearchConstants.ARTICLE_INDEX_NAME);
    }

    @Override
    public void deleteArticle(String articleId) {
        esService.deleteById(articleId, SearchConstants.ARTICLE_INDEX_NAME);
    }
}
