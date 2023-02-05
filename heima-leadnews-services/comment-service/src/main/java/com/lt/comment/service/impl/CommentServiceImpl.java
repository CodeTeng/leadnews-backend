package com.lt.comment.service.impl;

import com.alibaba.fastjson.JSON;
import com.lt.common.constants.article.HotArticleConstants;
import com.lt.model.mess.app.NewBehaviorDTO.BehaviorType;

import com.lt.aliyun.GreenTextScan;
import com.lt.comment.service.CommentHotService;
import com.lt.comment.service.CommentService;
import com.lt.exception.CustomException;
import com.lt.feigns.AdminFeign;
import com.lt.feigns.ArticleFeign;
import com.lt.feigns.UserFeign;
import com.lt.model.article.pojo.ApArticle;
import com.lt.model.comment.dto.CommentDTO;
import com.lt.model.comment.dto.CommentLikeDTO;
import com.lt.model.comment.dto.CommentSaveDTO;
import com.lt.model.comment.pojo.ApComment;
import com.lt.model.comment.pojo.ApCommentLike;
import com.lt.model.comment.vo.ApCommentVO;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.mess.app.NewBehaviorDTO;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import com.lt.utils.common.SensitiveWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 14:08
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private CommentHotService commentHotService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseResult saveComment(CommentSaveDTO commentSaveDTO) {
        // 1. 判断用户是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        // 2. 对评论内容进行审核 敏感词 阿里云
        String content = commentSaveDTO.getContent();
        // 2.1 数据库中的敏感词 这类敏感词不允许评论
        ResponseResult<List<String>> result = adminFeign.getAllSensitives();
        if (!result.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }
        List<String> sensitives = result.getData();
        SensitiveWordUtil.initMap(sensitives);
        Map<String, Integer> resultMap = SensitiveWordUtil.matchWords(content);
        if (resultMap.size() > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "内容包含敏感词：" + resultMap.keySet());
        }
        // 2.2 阿里云文本审核 这类将敏感词转换为 *
        try {
            Map map = greenTextScan.greenTextScan(content);
            String suggestion = (String) map.get("suggestion");
            if ("block".equals(suggestion)) {
                // 含有违规内容 评论内容替换
                content = (String) map.get("filteredContent");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (StringUtils.isBlank(content)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "内容涉嫌违规，无法评论");
        }
        // 3. 根据登录用户id查询当前用户
        Integer userId = apUser.getId();
        ResponseResult<ApUser> responseResult = userFeign.findUserById(userId);
        if (!result.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, "远程调用查询app用户接口失败");
        }
        // 4. 查询文章信息
        ResponseResult<ApArticle> articleResponseResult = articleFeign.findById(commentSaveDTO.getArticleId());
        if (!articleResponseResult.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }
        ApArticle apArticle = articleResponseResult.getData();
        ApUser user = responseResult.getData();
        // 5. 发表评论
        ApComment apComment = new ApComment();
        apComment.setAuthorId(userId);
        apComment.setAuthorName(user.getName());
        apComment.setContent(content);
        apComment.setArticleId(commentSaveDTO.getArticleId());
        apComment.setImage(user.getImage());
        apComment.setFlag((short) 0);
        apComment.setLikes(0);
        apComment.setReply(0);
        apComment.setType((short) 0);
        apComment.setChannelId(apArticle.getChannelId());
        apComment.setLongitude(null);
        apComment.setLatitude(null);
        apComment.setAddress(null);
        apComment.setOrd(null);
        apComment.setCreatedTime(new Date());
        apComment.setUpdatedTime(new Date());
        ApComment insert = mongoTemplate.insert(apComment);
        log.info("发表评论成功：{}", insert);
        // 发送消息
        NewBehaviorDTO newBehaviorDTO = new NewBehaviorDTO();
        newBehaviorDTO.setType(BehaviorType.COMMENT);
        newBehaviorDTO.setArticleId(commentSaveDTO.getArticleId());
        newBehaviorDTO.setAdd(1);
        rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE, JSON.toJSONString(newBehaviorDTO));
        log.info("发送评论行为消息成功：{}", newBehaviorDTO);
        return ResponseResult.okResult("发表评论成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult likeComment(CommentLikeDTO commentLikeDTO) {
        // 1. 判断用户是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        // 2. 根据评论id查询评论数据
        String commentId = commentLikeDTO.getCommentId();
        ApComment apComment = mongoTemplate.findOne(
                Query.query(Criteria.where("id").is(commentId)),
                ApComment.class
        );
        if (apComment == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该评论不存在");
        }
        // 3. 判断是点赞还是取消点赞
        // 引入分布式锁
        RLock lock = redissonClient.getLock("likes-lock");
        lock.lock();
        Integer lastLikes;
        Map<String, Object> map = new HashMap<>(1);
        try {
            Short operation = commentLikeDTO.getOperation();
            Integer userId = apUser.getId();
            ApCommentLike apCommentLike = mongoTemplate.findOne(
                    Query.query(Criteria.where("commentId").is(commentId).and("authorId").is(userId)),
                    ApCommentLike.class
            );
            if (operation.intValue() == 0) {
                // 4. 点赞
                // 4.1 判断用户是否已经点赞过
                if (apCommentLike != null) {
                    // 已经点赞过 无法重复点赞
                    return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "无法重复点赞");
                }
                apCommentLike = new ApCommentLike();
                apCommentLike.setCommentId(commentId);
                apCommentLike.setOperation(operation);
                apCommentLike.setAuthorId(userId);
                ApCommentLike insert = mongoTemplate.insert(apCommentLike);
                log.info("评论点赞成功：{}", insert);
                // 4.2 修改评论点赞数量
                apComment.setLikes(apComment.getLikes() + 1);
                ApComment save = mongoTemplate.save(apComment);
                log.info("评论点赞成功：{}", save);
                // 4.3 异步判断是否是热点文章
                if (apComment.getLikes() >= 10 && apComment.getFlag().intValue() == 0) {
                    commentHotService.hotCommentExecutor(apComment);
                }
                lastLikes = apComment.getLikes();
            } else {
                // 4. 取消点赞
                // 4.1 判断是否存在该评论点赞
                if (apCommentLike == null) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该评论你尚未点赞");
                }
                mongoTemplate.remove(apCommentLike);
                log.info("取消评论点赞成功");
                // 4.2 修改评论的点赞数量
                Integer likes = apComment.getLikes();
                if (likes - 1 < 0) {
                    likes = 0;
                } else {
                    likes--;
                }
                apComment.setLikes(likes);
                ApComment save = mongoTemplate.save(apComment);
                log.info("取消评论点赞成功：{}", save);
                lastLikes = apComment.getLikes();
            }
        } finally {
            lock.unlock();
        }
        map.put("likes", lastLikes);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult loadComment(CommentDTO commentDTO) {
        // 1. 根据条件分页查询评论列表
        // 判断当前是否是第一页评论
        List<ApComment> commentList = new ArrayList<>();
        if (commentDTO.getIndex().intValue() == 1) {
            // 是第一页
            // 1.1 先查询热点评论集合(最多5条) 点赞降序
            Query likeQuery = Query.query(Criteria.where("articleId").is(commentDTO.getArticleId()).and("flag").is((short) 1));
            Sort likes = Sort.by(Sort.Direction.DESC, "likes");
            likeQuery.with(likes);
            List<ApComment> hotCommentList = mongoTemplate.find(likeQuery, ApComment.class);
            if (hotCommentList.size() > 5) {
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "热评数超过5，数据异常");
            }
            commentDTO.setSize(commentDTO.getSize() - hotCommentList.size());
            // 1.2 查询第一页剩余普通评论 (条件: 文章id, flag=0, 时间降序, limit:新size)
            Long articleId = commentDTO.getArticleId();
            Date minDate = commentDTO.getMinDate();
            Query query = Query.query(Criteria.where("articleId").is(articleId).and("flag").is((short) 0).and("updatedTime").lt(minDate));
            // 分页 从 0 开始表示第一页
            Pageable pageable = PageRequest.of(0, commentDTO.getSize());
            query.with(pageable);
            // 降序
            Sort sort = Sort.by(Sort.Direction.DESC, "updatedTime");
            query.with(sort);
            List<ApComment> commonCommentList = mongoTemplate.find(query, ApComment.class);
            // 合并热点评论和普通评论
            commentList.addAll(hotCommentList);
            commentList.addAll(commonCommentList);
        } else {
            // 不是第一页
            Long articleId = commentDTO.getArticleId();
            Date minDate = commentDTO.getMinDate();
            Query query = Query.query(Criteria.where("articleId").is(articleId).and("flag").is((short) 0).and("updatedTime").lt(minDate));
            // 分页
            Short index = commentDTO.getIndex();
            Pageable pageable = PageRequest.of(index - 1, commentDTO.getSize());
            query.with(pageable);
            // 降序
            Sort sort = Sort.by(Sort.Direction.DESC, "updatedTime");
            query.with(sort);
            commentList = mongoTemplate.find(query, ApComment.class);
        }
        log.info("查询后的分页评论列表：{}", commentList);
        // 2. 判断当前用户是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
            // 3. 未登录 直接返回评论列表
            return ResponseResult.okResult(commentList);
        }
        // 4. 登录了 需要检查当前评论列表中哪些评论登陆人点赞过
        List<ApCommentVO> commentVoList = new ArrayList<>();
        // 4.1 根据当前列表中的评论id和当前用户查询出评论点赞数据
        List<String> commentIds = commentList.stream().map(ApComment::getId).collect(Collectors.toList());
        List<ApCommentLike> apCommentLikeList = mongoTemplate.find(
                Query.query(Criteria.where("authorId").is(apUser.getId()).and("commentId").in(commentIds)),
                ApCommentLike.class
        );
        // 4.2 将评论中的数据转换为 ApCommentVO 数据
        commentList.forEach(apComment -> {
            ApCommentVO apCommentVO = new ApCommentVO();
            BeanUtils.copyProperties(apComment, apCommentVO);
            if (apCommentLikeList.size() > 0) {
                for (ApCommentLike apCommentLike : apCommentLikeList) {
                    if (apCommentLike.getCommentId().equals(apComment.getId())) {
                        // 点赞过 转换数据
                        apCommentVO.setOperation((short) 0);
                        break;
                    }
                }
            }
            commentVoList.add(apCommentVO);
        });
        return ResponseResult.okResult(commentVoList);
    }
}
