package com.lt.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.model.wemedia.pojo.WmMaterial;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 22:49
 */
@Repository
public interface WmMaterialMapper extends BaseMapper<WmMaterial> {
    /**
     * 根据素材资源列表查询相关素材id
     *
     * @param urls   素材列表
     * @param userId 用户id
     * @return 素材id集合
     */
    List<Integer> selectRelationsIds(@Param("urls") List<String> urls, @Param("userId") Integer userId);
}
