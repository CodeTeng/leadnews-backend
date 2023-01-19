package com.lt.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.constants.wemedia.WemediaConstants;
import com.lt.file.service.FileStorageService;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.PageResponseResult;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.threadlocal.WmThreadLocalUtils;
import com.lt.model.wemedia.dto.WmMaterialDTO;
import com.lt.model.wemedia.pojo.WmMaterial;
import com.lt.model.wemedia.pojo.WmNewsMaterial;
import com.lt.model.wemedia.pojo.WmUser;
import com.lt.wemedia.mapper.WmMaterialMapper;
import com.lt.wemedia.mapper.WmNewsMaterialMapper;
import com.lt.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 22:50
 */
@Slf4j
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {
    @Autowired
    private FileStorageService fileStorageService;
    @Value("${file.oss.prefix}")
    String prefix;
    @Value("${file.oss.web-site}")
    String webSite;
    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 获取当前用户线程
        WmUser wmUser = WmThreadLocalUtils.getUser();
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验上传的文件的后缀名是否正确
        if (!checkFileSuffix(originalFilename)) {
            // 不正确 返回
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "请上传正确的素材格式,[jpg,jpeg,png,gif]");
        }

        String fileId = null;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = UUID.randomUUID().toString().replace("-", "");
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 上传文件
            fileId = fileStorageService.store(prefix, fileName + suffix, inputStream);
            log.info("阿里云OSS 文件 fileId: {}", fileId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("阿里云文件上传失败 uploadPicture error: {}", e.getMessage());
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "服务器繁忙，请稍微再试");
        }
        // 封装数据到素材库
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setType((short) 0);
        wmMaterial.setUserId(wmUser.getId());
        wmMaterial.setUrl(fileId);
        // 保存
        this.save(wmMaterial);
        // 返回给前端
        wmMaterial.setUrl(webSite + fileId);
        return ResponseResult.okResult(wmMaterial);
    }

    /**
     * 检查文件格式 目前仅仅支持jpg  jpeg  png  gif 图片的上传
     */
    private boolean checkFileSuffix(String originalFilename) {
        if (StringUtils.isBlank(originalFilename)) {
            return false;
        }
        List<String> allowSuffix = Arrays.asList("jpg", "jpeg", "png", "gif");
        for (String suffix : allowSuffix) {
            if (originalFilename.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseResult getList(WmMaterialDTO wmMaterialDTO) {
        // 获取当前用户线程
        WmUser wmUser = WmThreadLocalUtils.getUser();
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        Integer wmUserId = wmUser.getId();
        Short isCollection = wmMaterialDTO.getIsCollection();
        // 获取该用户素材
        Page<WmMaterial> page = new Page<>(wmMaterialDTO.getPage(), wmMaterialDTO.getSize());
        LambdaQueryWrapper<WmMaterial> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WmMaterial::getUserId, wmUserId)
                // 不为空 收藏 查询
                .eq(isCollection != null && isCollection.equals(WemediaConstants.COLLECT_MATERIAL), WmMaterial::getIsCollection, isCollection)
                .orderByDesc(WmMaterial::getCreatedTime);
        IPage<WmMaterial> resultPage = this.page(page, queryWrapper);
        List<WmMaterial> records = resultPage.getRecords();
        records.forEach(record -> record.setUrl(webSite + record.getUrl()));
        // 封装结果
        PageResponseResult pageResponseResult = new PageResponseResult(wmMaterialDTO.getPage(), wmMaterialDTO.getSize(), resultPage.getTotal());
        pageResponseResult.setData(records);
        return pageResponseResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delPicture(Integer id) {
        WmMaterial wmMaterial = this.getById(id);
        if (wmMaterial == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 判断当前素材是否被引用
        LambdaQueryWrapper<WmNewsMaterial> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WmNewsMaterial::getMaterialId, id);
        Integer count = wmNewsMaterialMapper.selectCount(queryWrapper);
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "该素材被引用，无法删除");
        }
        // 可以删除
        this.removeById(id);
        // 删除 oss 图片
        fileStorageService.delete(wmMaterial.getUrl());
        return ResponseResult.okResult();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateStatus(Integer id, Short type) {
        WmUser wmUser = WmThreadLocalUtils.getUser();
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        // 获取当前素材
        WmMaterial wmMaterial = this.getById(id);
        if (wmMaterial == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 获取当前用户 只允许用户自己收藏上传的素材
        Integer wmUserId = wmUser.getId();
        if (!wmMaterial.getUserId().equals(wmUserId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "只允许收藏自己上传的素材");
        }
        wmMaterial.setIsCollection(type);
        updateById(wmMaterial);
        return ResponseResult.okResult();
    }
}
