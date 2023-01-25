package com.lt.wemedia.controller.v1;

import com.lt.common.constants.wemedia.WemediaConstants;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.dto.DownOrUpNewsDTO;
import com.lt.model.wemedia.dto.NewsAuthDTO;
import com.lt.model.wemedia.dto.SubmitWmNewsDTO;
import com.lt.model.wemedia.dto.WmNewsPageDTO;
import com.lt.model.wemedia.pojo.WmNews;
import com.lt.wemedia.service.WmNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 12:17
 */
@Api(value = "自媒体文章管理API", tags = "自媒体文章管理API")
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {
    @Autowired
    private WmNewsService wmNewsService;

    @ApiOperation("根据条件分页查询文章列表")
    @PostMapping("/list")
    public ResponseResult findAll(@RequestBody WmNewsPageDTO wmNewsPageDTO) {
        if (wmNewsPageDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        wmNewsPageDTO.checkParam();
        return wmNewsService.getWmNewsPageList(wmNewsPageDTO);
    }

    @ApiOperation(value = "发表文章", notes = "发表文章，保存草稿，修改文章 共用的方法")
    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody SubmitWmNewsDTO submitWmNewsDTO) {
        // 参数校验
        if (submitWmNewsDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        String content = submitWmNewsDTO.getContent();
        String title = submitWmNewsDTO.getTitle();
        Integer channelId = submitWmNewsDTO.getChannelId();
        String labels = submitWmNewsDTO.getLabels();
        Short type = submitWmNewsDTO.getType();
        Short status = submitWmNewsDTO.getStatus();
        if (StringUtils.isAnyBlank(content, title, labels)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (channelId == null || type == null || status == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return wmNewsService.submitNews(submitWmNewsDTO);
    }

    @ApiOperation(value = "根据id查询自媒体文章")
    @GetMapping("/one/{id}")
    public ResponseResult findWmNewsById(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return wmNewsService.getWmNewsById(id);
    }

    @GetMapping("/del_news/{id}")
    @ApiOperation(value = "删除文章", notes = "删除之前需要发素材也给删除")
    public ResponseResult deleteWmNews(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return wmNewsService.deleteWmNews(id);
    }

    @PostMapping("/down_or_up")
    @ApiOperation(value = "文章上架或下架", notes = "enable 1-上架 0-下架")
    public ResponseResult downOrUpNews(@RequestBody DownOrUpNewsDTO downOrUpNewsDTO) {
        if (downOrUpNewsDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Integer id = downOrUpNewsDTO.getId();
        Short enable = downOrUpNewsDTO.getEnable();
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (enable == null || (!WemediaConstants.WM_NEWS_UP.equals(enable) && !WemediaConstants.WM_NEWS_DOWN.equals(enable))) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return wmNewsService.downOrUpNews(downOrUpNewsDTO);
    }

    @PostMapping("/list_vo")
    @ApiOperation("查询文章列表")
    public ResponseResult getNewsList(@RequestBody NewsAuthDTO newsAuthDTO) {
        if (newsAuthDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        newsAuthDTO.checkParam();
        return wmNewsService.getNewsList(newsAuthDTO);
    }

    @GetMapping("/one_vo/{id}")
    @ApiOperation("查询文章详细信息")
    public ResponseResult getWmNewsVo(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return wmNewsService.getWmNewsVo(id);
    }

    @ApiOperation("人工审核通过 状态:4")
    @PostMapping("/auth_pass")
    public ResponseResult authPass(@RequestBody NewsAuthDTO newsAuthDTO) {
        if (newsAuthDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return wmNewsService.updateStatus(WmNews.Status.ADMIN_SUCCESS.getCode(), newsAuthDTO);
    }

    @ApiOperation("人工审核失败 状态:2")
    @PostMapping("/auth_fail")
    public ResponseResult authFail(@RequestBody NewsAuthDTO newsAuthDTO) {
        if (newsAuthDTO == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return wmNewsService.updateStatus(WmNews.Status.FAIL.getCode(), newsAuthDTO);
    }

    @ApiOperation("根据id修改自媒体文章")
    @PutMapping("/update")
    public ResponseResult updateWmNews(@RequestBody WmNews wmNews) {
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        wmNewsService.updateById(wmNews);
        return ResponseResult.okResult();
    }
}
