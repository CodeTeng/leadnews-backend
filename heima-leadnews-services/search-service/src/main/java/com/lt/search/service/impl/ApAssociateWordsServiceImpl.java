package com.lt.search.service.impl;

import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.dto.UserSearchDTO;
import com.lt.model.search.pojo.ApAssociateWords;
import com.lt.search.service.ApAssociateWordsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 14:27
 */
@Service
public class ApAssociateWordsServiceImpl implements ApAssociateWordsService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult findAssociate(UserSearchDTO userSearchDto) {
        // 1. 参数校验
        String searchWords = userSearchDto.getSearchWords();
        if (StringUtils.isBlank(searchWords)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 2. 执行查询
        List<ApAssociateWords> associateWordsList = mongoTemplate.find(Query.query(Criteria.where("associateWords")
                .regex(".*" + searchWords + ".*")), ApAssociateWords.class);
        return ResponseResult.okResult(associateWordsList);
    }
}
