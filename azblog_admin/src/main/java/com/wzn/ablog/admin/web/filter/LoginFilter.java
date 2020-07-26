package com.wzn.ablog.admin.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzn.ablog.admin.config.AdminRsaKeyConfig;
import com.wzn.ablog.common.entity.Admin;
import com.wzn.ablog.common.utils.JwtUtils;
import com.wzn.ablog.common.utils.BlogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager manager;

    Logger logger = LoggerFactory.getLogger(getClass());

    private AdminRsaKeyConfig adminRsaKeyConfig;

    private RedisTemplate redisTemplate;

    public LoginFilter(AuthenticationManager manager, AdminRsaKeyConfig adminRsaKeyConfig,RedisTemplate redisTemplate) {
        this.manager = manager;
        this.adminRsaKeyConfig = adminRsaKeyConfig;
        this.redisTemplate = redisTemplate;
        //设置登录url
        super.setFilterProcessesUrl("/admin/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //前端ajax方式提交json数据处理，获取请求体json
            ServletInputStream inputStream = request.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            Admin user = objectMapper.readValue(inputStream, Admin.class);
            this.logger.debug("认证用户");
            this.logger.debug(user.getUsername()+user.getPassword());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return manager.authenticate(authenticationToken);
        } catch (Exception e) {
            this.logger.debug("用户名或密码错误");
            BlogUtils.respMsg(response, 500, "用户名或密码错误", null);
            e.printStackTrace();

            return null;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.logger.debug("认证失败");
        BlogUtils.respMsg(response, 500, "服务器内部错误", null);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.logger.debug("认证成功签发token");
        //获取登录用户的信息
        Admin currentAdmin = (Admin) authResult.getPrincipal();
        redisTemplate.opsForValue().set("userId" + currentAdmin.getId(), currentAdmin.getId());
        Admin admin = new Admin();
        //用户信息放入id和用户名，切记不要放密码
        admin.setId(currentAdmin.getId());
        admin.setUsername(currentAdmin.getUsername());
        admin.setRoles(currentAdmin.getRoles());

        logger.debug("getAuthorities:" + String.valueOf(authResult.getPrincipal()));
        //私钥生成token
        String token = JwtUtils.generateTokenExpireInMinutes(admin, adminRsaKeyConfig.getPrivateKey(), 24 * 60);
        //将token响应给客户端
        BlogUtils.respMsg(response, 200, "登录成功", token);
    }


}
