package com.lt.search.service.impl;

import com.lt.exception.CustomException;
import com.lt.feigns.BehaviorFeign;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.dto.HistorySearchDTO;
import com.lt.model.search.dto.UserSearchDTO;
import com.lt.model.search.pojo.ApUserSearch;
import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import com.lt.search.service.ApUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 13:34
 */
@Service
@Slf4j
public class ApUserSearchServiceImpl implements ApUserSearchService {
    @Autowired
    private BehaviorFeign behaviorFeign;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Async("taskExecutor")
    public void insert(UserSearchDTO userSearchDto) {
        // 1. 参数校验
        String searchWords = userSearchDto.getSearchWords();
        Integer entryId = userSearchDto.getEntryId();
        // 2. 查询行为实体
        ResponseResult<ApBehaviorEntry> responseResult = behaviorFeign.findByUserIdOrEquipmentId(entryId, userSearchDto.getEquipmentId());
        if (!responseResult.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
        }
        ApBehaviorEntry apBehaviorEntry = responseResult.getData();
        if (apBehaviorEntry == null) {
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        log.info("ApUserSearchServiceImpl insert entryId:{}, searchWords:{}", entryId, searchWords);
        // 3. 查询当前用户搜素的关键词是否存在
        ApUserSearch apUserSearch = mongoTemplate.findOne(Query.query(Criteria.where("entryId").is(entryId).and("keyword").is(searchWords)), ApUserSearch.class);
        // 3.1 存在 则更新时间 更新状态
        if (apUserSearch != null) {
            apUserSearch.setCreatedTime(new Date());
            mongoTemplate.save(apUserSearch);
            return;
        }
        // 3.2 不存在 新增
        apUserSearch = new ApUserSearch();
        apUserSearch.setKeyword(searchWords);
        apUserSearch.setEntryId(apBehaviorEntry.getId());
        apUserSearch.setCreatedTime(new Date());
        mongoTemplate.save(apUserSearch);
    }

    @Override
    public ResponseResult findUserSearch(UserSearchDTO userSearchDto) {
        if (userSearchDto.getMinBehotTime() == null) {
            userSearchDto.setMinBehotTime(new Date());
        }
        int size = 10;
        ApUser user = AppThreadLocalUtils.getUser();
        // 1 查询实体 60ed86609d88f41a1da5e770
        ResponseResult<ApBehaviorEntry> behaviorResult = behaviorFeign.findByUserIdOrEquipmentId(user == null ? null : user.getId(), userSearchDto.getEquipmentId());
        if (behaviorResult.getCode().intValue() != 0) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR, behaviorResult.getErrorMessage());
        }
        ApBehaviorEntry apBehaviorEntry = behaviorResult.getData();
        if (apBehaviorEntry == null) {
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 查询当前用户的搜索列表  按照时间倒序 .limit(userSearchDto.getPageSize())
        List<ApUserSearch> userSearchList = mongoTemplate.find(Query.query(Criteria.where("entryId").is(apBehaviorEntry.getId()).and("createdTime").lt(userSearchDto.getMinBehotTime()))
                .with(Sort.by(Sort.Direction.DESC, "createdTime")).limit(size), ApUserSearch.class);
        return ResponseResult.okResult(userSearchList);
    }

    @Override
    public ResponseResult delUserSearch(HistorySearchDTO historySearchDto) {
        if (historySearchDto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtils.getUser();
        ResponseResult<ApBehaviorEntry> behaviorEntryResult = behaviorFeign.findByUserIdOrEquipmentId(user == null ? null : user.getId(), historySearchDto.getEquipmentId());
        if (!behaviorEntryResult.checkCode()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR, "远程调用行为服务失败");
        }
        ApBehaviorEntry behaviorEntry = behaviorEntryResult.getData();
        if (behaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "行为实体数据不存在");
        }
        mongoTemplate.remove(Query.query(Criteria.where("id").is(historySearchDto.getId()).and("entryId").is(behaviorEntry.getId())), ApUserSearch.class);
        return ResponseResult.okResult();
    }
}
