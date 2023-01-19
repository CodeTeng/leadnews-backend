package com.lt.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.constants.wemedia.WemediaConstants;
import com.lt.exception.CustomException;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.PageResponseResult;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.threadlocal.WmThreadLocalUtils;
import com.lt.model.wemedia.dto.DownOrUpNewsDTO;
import com.lt.model.wemedia.dto.SubmitWmNewsDTO;
import com.lt.model.wemedia.dto.WmNewsPageDTO;
import com.lt.model.wemedia.pojo.WmNews;
import com.lt.model.wemedia.pojo.WmNewsMaterial;
import com.lt.model.wemedia.pojo.WmUser;
import com.lt.wemedia.mapper.WmMaterialMapper;
import com.lt.wemedia.mapper.WmNewsMapper;
import com.lt.wemedia.mapper.WmNewsMaterialMapper;
import com.lt.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 12:16
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Value("${file.oss.web-site}")
    private String webSite;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Override
    public ResponseResult getWmNewsPageList(WmNewsPageDTO wmNewsPageDTO) {
        String keyword = wmNewsPageDTO.getKeyword();
        Integer channelId = wmNewsPageDTO.getChannelId();
        Short status = wmNewsPageDTO.getStatus();
        Date beginPubDate = wmNewsPageDTO.getBeginPubDate();
        Date endPubDate = wmNewsPageDTO.getEndPubDate();
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNoneBlank(keyword), WmNews::getTitle, keyword)
                .eq(channelId != null, WmNews::getChannelId, channelId)
                .eq(status != null, WmNews::getStatus, status)
                .ge(beginPubDate != null, WmNews::getPublishTime, beginPubDate)
                .le(endPubDate != null, WmNews::getPublishTime, endPubDate);
        WmUser wmUser = WmThreadLocalUtils.getUser();
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        queryWrapper.eq(WmNews::getUserId, wmUser.getId())
                .orderByDesc(WmNews::getCreatedTime);
        Page<WmNews> page = new Page<>(wmNewsPageDTO.getPage(), wmNewsPageDTO.getSize());
        IPage<WmNews> resPage = this.page(page, queryWrapper);
        PageResponseResult pageResponseResult = new PageResponseResult(wmNewsPageDTO.getPage(), wmNewsPageDTO.getSize(), resPage.getTotal(), resPage.getRecords());
        pageResponseResult.setHost(webSite);
        return pageResponseResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult submitNews(SubmitWmNewsDTO submitWmNewsDTO) {
        // 判断用户是否登录
        WmUser wmUser = WmThreadLocalUtils.getUser();
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(submitWmNewsDTO, wmNews);
        // 判断文章的封面
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(submitWmNewsDTO.getType())) {
            // 自动封面
            wmNews.setType(null);
        }
        // 修改 images 封面集合 转为 string
        List<String> imageList = submitWmNewsDTO.getImages();
        String images = imageListToStr(imageList, webSite);
        wmNews.setImages(images);
        wmNews.setUserId(wmUser.getId());
        // 保存或者修改文章
        saveOrUpdateWmNews(wmNews);
        // 判断是否是草稿 草稿的话直接返回
        if (WemediaConstants.WM_NEWS_DRAFT_STATUS.equals(submitWmNewsDTO.getStatus())) {
            return ResponseResult.okResult();
        }
        // 抽取文章中关联的图片路径
        List<String> materials = parseContentImages(submitWmNewsDTO.getContent());
        // 保存文章内容中的图片和素材关系
        if (!CollectionUtils.isEmpty(materials)) {
            saveRelativeInfo(materials, wmNews.getId(), WemediaConstants.WM_CONTENT_REFERENCE);
        }
        // 保存文章封面中的图片和素材关系  封面可能是选择自动或者是无图
        saveRelativeInfoForCover(submitWmNewsDTO, materials, wmNews);
        return ResponseResult.okResult();
    }

    /**
     * 保存文章封面中的图片和素材关系
     *
     * @param submitWmNewsDTO 前端用户选择文章信息
     * @param urls            文章中的图片 url 集合
     * @param wmNews          文章信息
     */
    private void saveRelativeInfoForCover(SubmitWmNewsDTO submitWmNewsDTO, List<String> urls, WmNews wmNews) {
        // 用户选择的图片
        List<String> images = submitWmNewsDTO.getImages();
        // 自动封面
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(submitWmNewsDTO.getType())) {
            int size = urls.size();
            if (size > WemediaConstants.WM_NEWS_NONE_IMAGE && size < WemediaConstants.WM_NEWS_MANY_IMAGE) {
                // 单图
                images = urls.stream().limit(WemediaConstants.WM_NEWS_SINGLE_IMAGE).collect(Collectors.toList());
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
            } else if (size >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                // 多图
                images = urls.stream().limit(WemediaConstants.WM_NEWS_MANY_IMAGE).collect(Collectors.toList());
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
            } else {
                // 无图
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }
            if (images != null && images.size() > 0) {
                wmNews.setImages(imageListToStr(images, webSite));
            }
            // 更新文章信息
            this.updateById(wmNews);
        }
        // 保存图片列表和素材的关系
        if (images != null && images.size() > 0) {
            images = images.stream().map(x -> x.replace(webSite, "")).collect(Collectors.toList());
            saveRelativeInfo(images, wmNews.getId(), WemediaConstants.WM_IMAGE_REFERENCE);
        }

    }

    /**
     * 保存文章内容中的图片和素材关系
     *
     * @param urls   文章的 url 集合
     * @param newsId 文章id
     * @param type   类型 0：内容素材  1：封面素材
     */
    private void saveRelativeInfo(List<String> urls, Integer newsId, Short type) {
        // 查询文章内容对应图片的素材id
        List<Integer> ids = wmMaterialMapper.selectRelationsIds(urls, WmThreadLocalUtils.getUser().getId());
        // 判断素材是否缺失
        if (CollectionUtils.isEmpty(ids) || ids.size() < urls.size()) {
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST, "相关素材缺失,保存文章失败");
        }
        // 保存素材关系
        wmNewsMaterialMapper.saveRelations(ids, newsId, type);
    }

    /**
     * 抽取文章中关联的图片路径
     *
     * @param content 文章的所有内容
     * @return 文章中使用图片的 url 集合
     */
    private List<String> parseContentImages(String content) {
        List<Map> contents = JSON.parseArray(content, Map.class);
        // 遍历文章内容 将所有 type 为 image 的 value 获取出来 去除前缀路径
        return contents.stream()
                .filter(map -> map.get("type").equals(WemediaConstants.WM_NEWS_TYPE_IMAGE))
                .map(map -> (String) map.get("value"))
                .map(url -> url.replace(webSite, ""))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 保存或修改文章
     *
     * @param wmNews 文章内容信息
     */
    private void saveOrUpdateWmNews(WmNews wmNews) {
        wmNews.setPublishTime(new Date());
        // 上架
        wmNews.setEnable(WemediaConstants.WM_NEWS_UP);
        if (wmNews.getId() == null) {
            // 保存文章
            this.save(wmNews);
        } else {
            // 修改文章
            // 将当前文章和素材关系表数据删除
            wmNewsMaterialMapper.delete(new LambdaQueryWrapper<WmNewsMaterial>().eq(WmNewsMaterial::getNewsId, wmNews.getId()));
            // 再更新
            this.updateById(wmNews);
        }
    }

    /**
     * 图片列表转字符串，并去除图片前缀
     *
     * @param images  图片列表
     * @param webSite 图片前缀
     */
    private String imageListToStr(List<String> images, String webSite) {
        return images.stream()
                .map((url) -> url.replace(webSite, ""))
                .collect(Collectors.joining(","));
    }

    @Override
    public ResponseResult getWmNewsById(Integer id) {
        WmNews wmNews = this.getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该文章不存在");
        }
        ResponseResult responseResult = ResponseResult.okResult(wmNews);
        responseResult.setHost(webSite);
        return responseResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteWmNews(Integer id) {
        WmNews wmNews = this.getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该文章不存在");
        }
        // 不是本人不能删除
        Integer userId = wmNews.getUserId();
        if (!userId.equals(WmThreadLocalUtils.getUser().getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "只有本人可删除");
        }
        Short status = wmNews.getStatus();
        Short enable = wmNews.getEnable();
        // 已发布且已上架 不能删除
        if (status.equals(WmNews.Status.PUBLISHED.getCode()) && enable.equals(WemediaConstants.WM_NEWS_UP)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章已发布，不能删除");
        }
        // 删除关联
        wmNewsMaterialMapper.delete(new LambdaQueryWrapper<WmNewsMaterial>().eq(WmNewsMaterial::getNewsId, id));
        // 删除文章
        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult downOrUpNews(DownOrUpNewsDTO downOrUpNewsDTO) {
        WmNews wmNews = this.getById(downOrUpNewsDTO.getId());
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该文章不存在");
        }
        // 判断文章是否发布
        if (!wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "文章处于未发布状态，不能上下架");
        }
        // 修改文章状态
        wmNews.setEnable(downOrUpNewsDTO.getEnable());
        this.updateById(wmNews);
        // todo 同步 app 端文章配置
        return ResponseResult.okResult();
    }
}
