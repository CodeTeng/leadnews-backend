package com.lt.search.listen;

import com.lt.common.constants.message.NewsUpOrDownConstants;
import com.lt.feigns.ArticleFeign;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.vo.SearchArticleVO;
import com.lt.search.service.ArticleSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 12:55
 */
@Component
@Slf4j
public class ArticleAddOrRemoveListener {
    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private ArticleSearchService articleSearchService;

    @RabbitListener(queuesToDeclare = @Queue(NewsUpOrDownConstants.NEWS_UP_FOR_ES_QUEUE))
    public void addArticle(String articleId) {
        log.info("搜索微服务 接收到添加文章到索引库消息==> {}", articleId);
        ResponseResult<SearchArticleVO> responseResult = articleFeign.findArticle(Long.valueOf(articleId));
        if (!responseResult.checkCode()) {
            log.error("索引库添加失败 远程调用文章信息失败   文章id: {}", articleId);
        }
        SearchArticleVO searchArticleVo = responseResult.getData();
        if (searchArticleVo == null) {
            log.error("索引库添加失败 未获取到对应文章信息   文章id: {}", articleId);
        }
        try {
            articleSearchService.saveArticle(searchArticleVo);
            log.info("成功更新索引信息   add: {}", articleId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息消费确认失败   文章id: {}  原因: {}", articleId, e.getMessage());
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(value = NewsUpOrDownConstants.NEWS_DOWN_FOR_ES_QUEUE))
    public void removeArticle(String articleId) {
        log.info("搜索微服务 接收到删除索引库文章消息==> {}", articleId);
        try {
            articleSearchService.deleteArticle(articleId);
            log.info("成功更新索引信息   delete: {}", articleId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息消费确认失败   文章id: {}  原因: {}", articleId, e.getMessage());
        }
    }
}
