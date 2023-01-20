package com.lt.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.model.wemedia.dto.NewsAuthDTO;
import com.lt.model.wemedia.pojo.WmNews;
import com.lt.model.wemedia.vo.WmNewsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 12:15
 */
@Repository
public interface WmNewsMapper extends BaseMapper<WmNews> {
    List<WmNewsVO> getListAndPage(@Param("dto") NewsAuthDTO newsAuthDTO);

    long getListCount(@Param("dto") NewsAuthDTO newsAuthDTO);
}
