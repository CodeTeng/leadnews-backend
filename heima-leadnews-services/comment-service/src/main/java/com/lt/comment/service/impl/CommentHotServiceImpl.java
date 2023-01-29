package com.lt.comment.service.impl;

import com.lt.comment.service.CommentHotService;
import com.lt.model.comment.pojo.ApComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 18:23
 */
@Service
@Slf4j
public class CommentHotServiceImpl implements CommentHotService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 处理热点评论 @Async 代表异步执行  taskExecutor是我们在配置中定义的线程池
     *
     * @param apComment 评论信息
     */
    @Override
    @Async("taskExecutor")
    public void hotCommentExecutor(ApComment apComment) {
        log.info("异步计算热点文章==================> 开始");
        // 1. 查询当前文章下的所有热点评论集合
        Long articleId = apComment.getArticleId();
        Query query = Query.query(Criteria.where("flag").is((short) 1).and("articleId").is(articleId));
        Sort sort = Sort.by(Sort.Direction.DESC, "likes");
        query.with(sort);
        List<ApComment> hotCommenList = mongoTemplate.find(query, ApComment.class);
        // 2. 如果为空或评论数量小于5 直接将当前评论修改为热点评论
        if (hotCommenList.isEmpty() || hotCommenList.size() <= 5) {
            apComment.setFlag((short) 1);
            mongoTemplate.save(apComment);
        } else {
            // 3. 热评数量大于5
            // 3.1 获取当前热评集合中最少点赞数量的热评
            ApComment comment = hotCommenList.stream().min(Comparator.comparing(ApComment::getLikes)).get();
            // 3.2 判断当前点赞数量的评论和最少点赞的对比 谁的多 修改为热评 另一个改为普通评论
            if (comment.getLikes() < apComment.getLikes()) {
                apComment.setFlag((short) 1);
                mongoTemplate.save(apComment);
                comment.setFlag((short) 0);
                mongoTemplate.save(comment);
            }
        }
        log.info("异步计算热点文章==================> 结束");
    }
}
