package com.lt.article.job;

import com.alibaba.fastjson.JSON;
import com.lt.article.service.HotArticleService;
import com.lt.common.constants.article.HotArticleConstants;
import com.lt.exception.CustomException;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.mess.app.AggBehaviorDTO;
import com.lt.model.mess.app.NewBehaviorDTO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/30 16:34
 */
@Component
@Slf4j
public class UpdateHotArticleJob {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private HotArticleService hotArticleService;

    @XxlJob("updateHotArticleJob")
    public ReturnT updateHotArticleHandler(String param) {
        log.info("热文章分值更新 调度任务开始执行....");
        // 1. 获取 redis List 中的行为数据
        List<NewBehaviorDTO> behaviorList = getRedisBehaviorList();
        if (CollectionUtils.isEmpty(behaviorList)) {
            log.info("热文章分值更新: 太冷清了 未产生任何文章行为 调度任务完成....");
            return ReturnT.SUCCESS;
        }
        // 2. 将数据按照文章分组  进行聚合统计 得到待更新的数据列表
        List<AggBehaviorDTO> aggBehaviorList = getAddBehaviorList(behaviorList);
        if (CollectionUtils.isEmpty(aggBehaviorList)) {
            log.info("热文章分值更新: 太冷清了 未产生任何文章行为 调度任务完成....");
            return ReturnT.SUCCESS;
        }
        // 3. 根据文章聚合数据 修改文章热度
        aggBehaviorList.forEach(hotArticleService::updateApArticleHot);
        log.info("热文章分值更新 调度任务完成....");
        return ReturnT.SUCCESS;
    }

    /**
     * 将数据按照文章分组  进行聚合统计 得到待更新的数据列表
     */
    private List<AggBehaviorDTO> getAddBehaviorList(List<NewBehaviorDTO> behaviorList) {
        List<AggBehaviorDTO> list = new ArrayList<>();
        // 1. 按照文章id进行分组
        Map<Long, List<NewBehaviorDTO>> groupMap = behaviorList.stream().collect(Collectors.groupingBy(NewBehaviorDTO::getArticleId));
        // 2. 将每个文章行为封装成聚合数据 然后返回
        groupMap.forEach((articleId, messList) -> {
            Optional<AggBehaviorDTO> optional = messList.stream().map(behavior -> {
                // 将每个行为数据  都封装为聚合行为类型
                AggBehaviorDTO aggBehaviorDTO = new AggBehaviorDTO();
                aggBehaviorDTO.setArticleId(articleId);
                switch (behavior.getType()) {
                    case LIKES:
                        aggBehaviorDTO.setLike(behavior.getAdd());
                        break;
                    case VIEWS:
                        aggBehaviorDTO.setView(behavior.getAdd());
                        break;
                    case COMMENT:
                        aggBehaviorDTO.setComment(behavior.getAdd());
                        break;
                    case COLLECTION:
                        aggBehaviorDTO.setCollect(behavior.getAdd());
                        break;
                    default:
                        break;
                }
                return aggBehaviorDTO;
            }).reduce((a1, a2) -> {
                // 进行聚合封装
                a1.setCollect(a1.getCollect() + a2.getCollect());
                a1.setLike(a1.getLike() + a2.getLike());
                a1.setView(a1.getView() + a2.getView());
                a1.setComment(a1.getComment() + a2.getComment());
                return a1;
            });
            optional.ifPresent(list::add);
        });
        return list;
    }

    /**
     * 获取 redis List 中的行为数据
     */
    private List<NewBehaviorDTO> getRedisBehaviorList() {
        try {
            // 1. 设置 lua 脚本
            DefaultRedisScript<List> redisScript = new DefaultRedisScript<>();
            // 设置脚本返回结果
            redisScript.setResultType(List.class);
            // lua文件存放在resources目录下的redis文件夹内
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis.lua")));
            // 2. 执行 lua 脚本
            List<String> result = stringRedisTemplate.execute(redisScript, Arrays.asList(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_LIST));
            // 3. 将查询到的数据转换为 NewBehaviorDTO 数据
            return result.stream().map(jsonStr -> JSON.parseObject(jsonStr, NewBehaviorDTO.class)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR, "执行lua脚本失败");
        }
    }
}
