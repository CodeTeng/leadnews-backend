package com.lt.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.dto.WmMaterialDTO;
import com.lt.model.wemedia.pojo.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 22:49
 */
public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 上传图片
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 查询素材列表
     *
     * @param wmMaterialDTO 分页请求DTO
     */
    ResponseResult getList(WmMaterialDTO wmMaterialDTO);

    /**
     * 删除素材
     */
    ResponseResult delPicture(Integer id);

    /**
     * 收藏与取消收藏
     */
    ResponseResult updateStatus(Integer id, Short type);
}
