package com.wzn.blog.gateway.filter;

import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.utils.JwtUtils;
import com.wzn.ablog.common.utils.RsaKeyConfig;
import com.wzn.ablog.common.vo.Payload;
import com.wzn.blog.gateway.entiry.AdminInfo;
import com.wzn.blog.gateway.entiry.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JwtVerifyFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RsaKeyConfig rsaKeyConfig;

    /**
     * 过滤逻辑
     *
     * @param exchange 请求和响应的上下文，可获取request和response
     * @param chain
     * @return 返回 chain.filter(exchange)可放行
     */
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        //登录请求放行
        if (path.contains("/admin/login")
                || path.contains("/admin/apply")
                || path.contains("/article/v2/api-docs")
                || path.contains("/admin/v2/api-docs")
                || path.contains("/base/v2/api-docs")
                || path.contains("/admin/v2/api-docs")
                || path.contains("/echarts/v2/api-docs")
                || path.contains("/search/v2/api-docs")
                || path.contains("/link/v2/api-docs")
                || path.contains("/admin/code")) {
            return chain.filter(exchange);
        }

        List<String> list = request.getHeaders().get(AzContants.TOKEN_HEAD);
        //获取token并解析校验
        String token = list.get(0);
        log.debug(token);
        Payload<AdminInfo> tokenInfo = JwtUtils.getInfoFromToken(token, rsaKeyConfig.getPublicKey(), AdminInfo.class);
        AdminInfo userInfo = tokenInfo.getUserInfo();
        List<Role> roles = userInfo.getRoles();
        List<String> roleName = new ArrayList<>();
        for (Role role : roles) {
            roleName.add(role.getRole_name());
        }
        redisTemplate.opsForValue().set("roles" + userInfo.getId(), roleName);
        return chain.filter(exchange);
    }

    /**
     * 执行顺序，值小优先
     *
     * @return
     */
    public int getOrder() {
        return 0;
    }
}