package com.lt.search.service;

import com.lt.model.common.vo.ResponseResult;
import com.lt.model.search.dto.UserSearchDTO;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 14:27
 */
public interface ApAssociateWordsService {

    /**
     * 联想词
     */
    ResponseResult findAssociate(UserSearchDTO userSearchDto);

}
