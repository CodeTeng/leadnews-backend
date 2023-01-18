package com.lt.wemedia.filter;

import com.alibaba.fastjson.JSON;
import com.lt.utils.common.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 21:27
 */
@Component
@Slf4j
@Order(0)
public class AuthorizeFilter implements GlobalFilter {
    /**
     * 白名单 url 路径
     */
    private static List<String> urlList = new ArrayList<>();

    static {
        urlList.add("/login/in");
        urlList.add("/v2/api-docs");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 判断当前是否是白名单请求
        ServerHttpRequest request = exchange.getRequest();
        String reqUrl = request.getURI().getPath();
        for (String url : urlList) {
            if (reqUrl.contains(url)) {
                // 直接放行
                return chain.filter(exchange);
            }
        }
        // 2. 获取请求头 token
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
            // 想客户端返回错误信息 拦截
            return writeMessage(exchange, "请登录");
        }
        try {
            // 3. 判断 token 是否正确
            Claims claims = AppJwtUtil.getClaimsBody(token);
            int res = AppJwtUtil.verifyToken(claims);
            // token 过期 无效
            if (res > 0) {
                // 返回错误信息 拦截
                return writeMessage(exchange, "认证失效，请重新登录");
            }
            Integer id = claims.get("id", Integer.class);
            log.info("token网关校验成功\tid:{}\tURL:{}", id, request.getURI().getPath());
            // 4. 获取到的id值，重写到请求头中，传递到要调用的微服务中
            request.mutate().header("userId", String.valueOf(id)).build();
            exchange.mutate().request(request).build();
            // 5. 放行
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("token 校验失败: {}", e.getMessage());
            // 无效 token 拦截
            return writeMessage(exchange, "认证失效，请重新登录");
        }
    }

    /**
     * 返回错误提示信息
     */
    private Mono<Void> writeMessage(ServerWebExchange exchange, String message) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", HttpStatus.UNAUTHORIZED.value());
        map.put("errorMessage", message);
        ServerHttpResponse response = exchange.getResponse();
        // 设置状态码
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 设置返回类型
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 设置返回数据
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONBytes(map));
        // 响应数据回浏览器
        return response.writeWith(Flux.just(dataBuffer));
    }
}
