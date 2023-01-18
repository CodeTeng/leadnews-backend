package com.lt.wemedia.controller.v1;

import com.lt.common.constants.wemedia.WemediaConstants;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.dto.WmMaterialDTO;
import com.lt.wemedia.service.WmMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 22:50
 */
@Api(value = "素材管理", tags = "素材管理Controller")
@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {
    @Autowired
    private WmMaterialService materialService;

    @ApiOperation("上传素材")
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "请上传正确的文件");
        }
        return materialService.uploadPicture(multipartFile);
    }

    @PostMapping("/list")
    @ApiOperation("查询素材列表")
    public ResponseResult getList(@RequestBody WmMaterialDTO wmMaterialDTO) {
        if (wmMaterialDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        wmMaterialDTO.checkParam();
        return materialService.getList(wmMaterialDTO);
    }

    @ApiOperation(value = "删除素材", notes = "根据ID删除素材,有关联的素材不可以删除")
    @GetMapping("/del_picture/{id}")
    public ResponseResult delPicture(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return materialService.delPicture(id);
    }

    @ApiOperation("取消收藏素材")
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollectionMaterial(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return materialService.updateStatus(id, WemediaConstants.CANCEL_COLLECT_MATERIAL);
    }

    @ApiOperation("收藏素材")
    @GetMapping("/collect/{id}")
    public ResponseResult collectionMaterial(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return materialService.updateStatus(id, WemediaConstants.COLLECT_MATERIAL);
    }
}
