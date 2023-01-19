package com.lt.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.model.wemedia.pojo.WmNewsMaterial;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 23:30
 */
@Repository
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {
    /**
     * 保存文章和素材的关联关系
     *
     * @param wmMaterialIds 素材id集合
     * @param newsId        文章ID
     * @param type          文章封面类型  0 内容引用  1 封面引用
     */
    void saveRelations(@Param("wmMaterialIds") List<Integer> wmMaterialIds,
                       @Param("newsId") Integer newsId,
                       @Param("type") Short type);
}
