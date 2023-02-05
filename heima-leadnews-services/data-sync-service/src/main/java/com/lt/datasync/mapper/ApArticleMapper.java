package com.lt.datasync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.model.article.pojo.ApArticle;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 16:53
 */
@Repository
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    @Select("select aa.* from ap_article aa left join ap_article_config" +
            " aac on aa.id = aac.article_id where aac.is_delete !=1 and aac.is_down !=1")
    List<ApArticle> findAllArticles();
}
