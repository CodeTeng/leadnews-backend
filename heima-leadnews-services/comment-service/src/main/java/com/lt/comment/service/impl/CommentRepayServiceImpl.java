package com.lt.comment.service.impl;

import com.lt.aliyun.GreenTextScan;
import com.lt.comment.service.CommentRepayService;
import com.lt.exception.CustomException;
import com.lt.feigns.AdminFeign;
import com.lt.feigns.UserFeign;
import com.lt.model.comment.dto.CommentRepayDTO;
import com.lt.model.comment.dto.CommentRepayLikeDTO;
import com.lt.model.comment.dto.CommentRepaySaveDTO;
import com.lt.model.comment.pojo.ApComment;
import com.lt.model.comment.pojo.ApCommentLike;
import com.lt.model.comment.pojo.ApCommentRepay;
import com.lt.model.comment.pojo.ApCommentRepayLike;
import com.lt.model.comment.vo.ApCommentRepayVO;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import com.lt.utils.common.SensitiveWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 17:16
 */
@Service
@Slf4j
public class CommentRepayServiceImpl implements CommentRepayService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private UserFeign userFeign;

    @Override
    public ResponseResult loadCommentRepay(CommentRepayDTO dto) {
        // 1. 根据评论id和时间查询评论回复信息
        String commentId = dto.getCommentId();
        Date minDate = dto.getMinDate();
        Query query = Query.query(Criteria.where("commentId").is(commentId).and("createdTime").lt(minDate));
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        query.with(sort);
        query.limit(dto.getSize());
        List<ApCommentRepay> apCommentRepayList = mongoTemplate.find(query, ApCommentRepay.class);
        // 2. 检查是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
            // 2.1 未登录 直接返回
            return ResponseResult.okResult(apCommentRepayList);
        }
        // 3 登录了 需要判断那些回复点赞过
        // 3.1 查询当前回复的点赞列表
        List<String> commentRepayIds = apCommentRepayList.stream().map(ApCommentRepay::getId).collect(Collectors.toList());
        Integer userId = apUser.getId();
        List<ApCommentRepayLike> apCommentRepayLikeList = mongoTemplate.find(
                Query.query(Criteria.where("authorId").is(userId).and("commentRepayId").in(commentRepayIds)),
                ApCommentRepayLike.class
        );
        // 3.2 遍历回复列表 将回复信息封装为VO
        List<ApCommentRepayVO> apCommentRepayVOList = apCommentRepayList.stream().map(apCommentRepay -> {
            ApCommentRepayVO apCommentRepayVO = new ApCommentRepayVO();
            BeanUtils.copyProperties(apCommentRepay, apCommentRepayVO);
            for (ApCommentRepayLike apCommentRepayLike : apCommentRepayLikeList) {
                if (apCommentRepayLike.getCommentRepayId().equals(apCommentRepay.getId())) {
                    apCommentRepayVO.setOperation((short) 0);
                    break;
                }
            }
            return apCommentRepayVO;
        }).collect(Collectors.toList());
        return ResponseResult.okResult(apCommentRepayVOList);
    }

    @Override
    public ResponseResult saveCommentRepay(CommentRepaySaveDTO dto) {
        // 1. 判断用户是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        // 2. 对评论内容进行审核 敏感词 阿里云
        String content = dto.getContent();
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
        ApUser user = responseResult.getData();
        Query query = Query.query(Criteria.where("id").is(dto.getCommentId()));
        ApComment apComment = mongoTemplate.findOne(query, ApComment.class);
        if (apComment == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "被回复的对应评论不存在，id:" + dto.getCommentId());
        }
        // 4. 发表评论
        ApCommentRepay apCommentRepay = new ApCommentRepay();
        apCommentRepay.setAuthorId(userId);
        apCommentRepay.setAuthorName(user.getName());
        apCommentRepay.setCommentId(dto.getCommentId());
        apCommentRepay.setContent(content);
        apCommentRepay.setLikes(0);
        apCommentRepay.setLongitude(null);
        apCommentRepay.setLatitude(null);
        apCommentRepay.setAddress(null);
        apCommentRepay.setCreatedTime(new Date());
        apCommentRepay.setUpdatedTime(new Date());
        ApCommentRepay insert = mongoTemplate.insert(apCommentRepay);
        log.info("发表评论成功：{}", insert);
        // 5. 修改评论信息中回复数量 + 1
        apComment.setReply(apComment.getReply() + 1);
        mongoTemplate.save(apComment);
        return ResponseResult.okResult("发表回复评论成功");
    }

    @Override
    public ResponseResult saveCommentRepayLike(CommentRepayLikeDTO dto) {
        // 1. 判断用户是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请先登录");
        }
        // 2. 根据评论id查询回复评论数据
        String commentRepayId = dto.getCommentRepayId();
        ApCommentRepay apCommentRepay = mongoTemplate.findOne(
                Query.query(Criteria.where("id").is(commentRepayId)),
                ApCommentRepay.class
        );
        if (apCommentRepay == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该评论不存在");
        }
        // 3. 判断是点赞还是取消点赞
        Short operation = dto.getOperation();
        Integer userId = apUser.getId();
        ApCommentRepayLike apCommentRepayLike = mongoTemplate.findOne(
                Query.query(Criteria.where("commentRepayId").is(commentRepayId).and("authorId").is(userId)),
                ApCommentRepayLike.class
        );
        if (operation.intValue() == 0) {
            // 4. 点赞
            // 4.1 判断用户是否已经点赞过
            if (apCommentRepayLike != null) {
                // 已经点赞过 无法重复点赞
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "无法重复点赞");
            }
            apCommentRepayLike = new ApCommentRepayLike();
            apCommentRepayLike.setCommentRepayId(commentRepayId);
            apCommentRepayLike.setOperation(operation);
            apCommentRepayLike.setAuthorId(userId);
            ApCommentRepayLike insert = mongoTemplate.insert(apCommentRepayLike);
            log.info("回复评论点赞成功：{}", insert);
            // 4.2 修改回复评论点赞数量
            apCommentRepay.setLikes(apCommentRepay.getLikes() + 1);
            ApCommentRepay save = mongoTemplate.save(apCommentRepay);
            log.info("回复评论点赞成功：{}", save);
            Map<String, Object> map = new HashMap<>(1);
            map.put("likes", apCommentRepay.getLikes());
            return ResponseResult.okResult(map);
        } else {
            // 4. 取消点赞
            // 4.1 判断是否存在该评论点赞
            if (apCommentRepayLike == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该评论你尚未点赞");
            }
            mongoTemplate.remove(apCommentRepayLike);
            log.info("取消评论点赞成功");
            // 4.2 修改回复评论的点赞数量
            Integer likes = apCommentRepay.getLikes();
            if (likes - 1 < 0) {
                likes = 0;
            } else {
                likes--;
            }
            apCommentRepay.setLikes(likes);
            ApCommentRepay save = mongoTemplate.save(apCommentRepay);
            log.info("取消回复评论点赞成功：{}", save);
            Map<String, Object> map = new HashMap<>(1);
            map.put("likes", apCommentRepay.getLikes());
            return ResponseResult.okResult(map);
        }
    }
}