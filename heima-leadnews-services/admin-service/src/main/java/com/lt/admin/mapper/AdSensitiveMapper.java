package com.lt.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.model.admin.pojo.AdSensitive;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 16:02
 */
@Repository
public interface AdSensitiveMapper extends BaseMapper<AdSensitive> {
    @Select("select sensitives from ad_sensitive")
    List<String> getAllSensitives();
}
