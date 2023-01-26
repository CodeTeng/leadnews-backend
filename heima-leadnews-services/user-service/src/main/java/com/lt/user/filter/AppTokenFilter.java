package com.lt.user.filter;

import com.lt.model.threadlocal.AppThreadLocalUtils;
import com.lt.model.user.pojo.ApUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 17:52
 */
@Slf4j
@Order(1)
@WebFilter(filterName = "appTokenFilter", urlPatterns = "/*")
@Component
public class AppTokenFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 1. 获取请求对象
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 2. 获取请求头中的 userId 信息
        String userId = request.getHeader("userId");
        if (StringUtils.isNotBlank(userId)) {
            // 3. 如果userId有值存入到ThreadLocal中
            ApUser apUser = new ApUser();
            apUser.setId(Integer.valueOf(userId));
            AppThreadLocalUtils.setUser(apUser);
        }
        // 4. 放行
        filterChain.doFilter(request, servletResponse);
        // 5. 清空登录信息
        AppThreadLocalUtils.clear();
    }
}
