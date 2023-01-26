package com.lt.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.model.article.dto.ArticleHomeDTO;
import com.lt.model.article.pojo.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    /**
     * 查询文章列表
     *
     * @param dto  加载文章DTO
     * @param type 0：加载更多   1：加载最新
     * @return 文章集合
     */
    List<ApArticle> loadArticleList(@Param("dto") ArticleHomeDTO dto, @Param("type") Short type);
}