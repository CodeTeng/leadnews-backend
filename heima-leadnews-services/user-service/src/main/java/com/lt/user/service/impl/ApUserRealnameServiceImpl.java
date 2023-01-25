package com.lt.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.constants.admin.AdminConstants;
import com.lt.exception.CustomException;
import com.lt.feigns.ArticleFeign;
import com.lt.feigns.WemediaFeign;
import com.lt.model.article.pojo.ApAuthor;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.PageResponseResult;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.dto.AuthDTO;
import com.lt.model.user.pojo.ApUser;
import com.lt.model.user.pojo.ApUserRealname;
import com.lt.model.wemedia.pojo.WmUser;
import com.lt.user.mapper.ApUserMapper;
import com.lt.user.mapper.ApUserRealnameMapper;
import com.lt.user.service.ApUserRealnameService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 11:41
 */
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {
    @Autowired
    private ApUserMapper apUserMapper;

    @Autowired
    private WemediaFeign wemediaFeign;

    @Autowired
    private ArticleFeign articleFeign;

    @Override
    public ResponseResult getApUserListByStatus(AuthDTO authDTO) {
        // 分页查询
        Page<ApUserRealname> page = new Page<>(authDTO.getPage(), authDTO.getSize());
        LambdaQueryWrapper<ApUserRealname> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(authDTO.getStatus() != null, ApUserRealname::getStatus, authDTO.getStatus());
        IPage<ApUserRealname> resultPage = this.page(page, queryWrapper);
        return new PageResponseResult(authDTO.getPage(), authDTO.getSize(), resultPage.getTotal(), resultPage.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateStatusById(AuthDTO authDTO, Short status) {
        // 1. 获取相关信息
        // 1.1 获取 APP 用户认证信息
        ApUserRealname userRealname = this.getById(authDTO.getId());
        if (userRealname == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "APP用户认证信息不存在");
        }
        // 1.2 获取 APP 用户信息
        ApUser apUser = apUserMapper.selectById(userRealname.getUserId());
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "APP用户信息不存在");
        }
        // 2. 判断状态
        Short userRealnameStatus = userRealname.getStatus();
        if (!AdminConstants.WAIT_AUTH.equals(userRealnameStatus)) {
            // 实名用户信息非待审核状态 无法审核
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW, "用户处于非审核状态，无法审核");
        }
        // 3. 更新认证用户信息
        userRealname.setStatus(status);
        if (StringUtils.isNotBlank(authDTO.getMsg())) {
            userRealname.setReason(authDTO.getMsg());
        }
        this.updateById(userRealname);
        // 4. 如果认证状态为通过
        if (AdminConstants.PASS_AUTH.equals(status)) {
            // 4.2 创建自媒体账户
            WmUser wmUser = createWmUser(apUser);
            // 4.3 创建作者信息
            createApAuthor(wmUser);
        }
        return ResponseResult.okResult();
    }

    /**
     * 创建作者信息
     */
    private void createApAuthor(WmUser wmUser) {
        ResponseResult<ApAuthor> responseResult = articleFeign.findByUserId(wmUser.getApUserId());
        if (!responseResult.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, responseResult.getErrorMessage());
        }
        ApAuthor apAuthor = responseResult.getData();
        if (apAuthor != null) {
            throw new CustomException(AppHttpCodeEnum.DATA_EXIST, "作者信息已存在");
        }
        apAuthor = new ApAuthor();
        apAuthor.setName(wmUser.getName());
        apAuthor.setWmUserId(wmUser.getId());
        apAuthor.setUserId(wmUser.getApUserId());
        apAuthor.setType(AdminConstants.AUTHOR_TYPE);
        ResponseResult result = articleFeign.save(apAuthor);
        if (!result.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, responseResult.getErrorMessage());
        }
    }

    /**
     * 创建自媒体账号
     */
    private WmUser createWmUser(ApUser apUser) {
        // 查询自媒体账号是否存在（APP端用户密码和自媒体密码一致）
        ResponseResult<WmUser> responseResult = wemediaFeign.findByName(apUser.getName());
        if (!responseResult.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, responseResult.getErrorMessage());
        }
        WmUser wmUser = responseResult.getData();
        if (wmUser != null) {
            throw new CustomException(AppHttpCodeEnum.DATA_EXIST, "自媒体账户已存在");
        }
        wmUser = new WmUser();
        wmUser.setName(apUser.getName());
        wmUser.setSalt(apUser.getSalt());
        wmUser.setImage(apUser.getImage());
        wmUser.setPassword(apUser.getPassword());
        wmUser.setPhone(apUser.getPhone());
        wmUser.setType(0);
        wmUser.setApUserId(apUser.getId());
        wmUser.setStatus(AdminConstants.PASS_AUTH.intValue());
        ResponseResult<WmUser> result = wemediaFeign.save(wmUser);
        if (!result.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, responseResult.getErrorMessage());
        }
        return result.getData();
    }
}
