package com.lt.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.admin.dto.ChannelDTO;
import com.lt.model.admin.pojo.AdChannel;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/13 19:22
 */
public interface AdChannelService extends IService<AdChannel> {

    /**
     * 根据条件分页查询频道列表，按照顺序编号升序排序
     *
     * @param channelDTO 分页条件实体
     */
    ResponseResult getChannelByNameAndPage(ChannelDTO channelDTO);
}
