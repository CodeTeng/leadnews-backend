package com.lt.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.dto.DownOrUpNewsDTO;
import com.lt.model.wemedia.dto.SubmitWmNewsDTO;
import com.lt.model.wemedia.dto.WmNewsPageDTO;
import com.lt.model.wemedia.pojo.WmNews;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 12:15
 */
public interface WmNewsService extends IService<WmNews> {
    /**
     * 分页查询所有自媒体文章
     *
     * @param wmNewsPageDTO 分页请求DTO
     */
    ResponseResult getWmNewsPageList(WmNewsPageDTO wmNewsPageDTO);

    /**
     * 自媒体文章发布、修改、保存
     */
    ResponseResult submitNews(SubmitWmNewsDTO submitWmNewsDTO);

    /**
     * 根据文章id查询文章
     */
    ResponseResult getWmNewsById(Integer id);

    /**
     * 删除文章
     */
    ResponseResult deleteWmNews(Integer id);

    /**
     * 文章上架、下架操作
     */
    ResponseResult downOrUpNews(DownOrUpNewsDTO downOrUpNewsDTO);
}
