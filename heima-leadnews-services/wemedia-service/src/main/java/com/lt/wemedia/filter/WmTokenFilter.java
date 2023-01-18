package com.lt.wemedia.filter;

import com.lt.model.threadlocal.WmThreadLocalUtils;
import com.lt.model.wemedia.pojo.WmUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description: 请求过滤器
 * @author: ~Teng~
 * @date: 2023/1/18 22:41
 */
@Component
@WebFilter(value = "wmTokenFilter", urlPatterns = "/*")
@Order(-1)
@Slf4j
public class WmTokenFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 获取请求头中的 userId
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String userId = request.getHeader("userId");
        if (StringUtils.isNotBlank(userId)) {
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            // 保存在当前线程中
            WmThreadLocalUtils.setUser(wmUser);
        }
        // 如果没有则直接放行
        filterChain.doFilter(request, servletResponse);
        // 过滤器处理完毕后  清空用户信息 重点
        WmThreadLocalUtils.clear();
    }
}
