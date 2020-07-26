package com.wzn.ablog.article.utils;

import com.wzn.ablog.common.entity.Admin;
import com.wzn.ablog.common.utils.JwtUtils;
import com.wzn.ablog.common.utils.RsaKeyConfig;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.PageResult;
import com.wzn.ablog.common.vo.Payload;
import com.wzn.ablog.common.vo.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@Component
@Aspect
public class AuthorityAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RsaKeyConfig rsaKeyConfig;

    @Pointcut("execution(* com.wzn.ablog.article.web.*.*(..))")
    public void pointCut() {
    }

    @Pointcut("@annotation(com.wzn.ablog.common.annotation.Authorized)")
    public void authorized() {
    }

    @Around("authorized()")
    public Object GrantedAuthorityRoot(ProceedingJoinPoint joinPoint) {
        String userId = getUserId(request);
        List<String> list = (List<String>) redisTemplate.opsForValue().get("roles" + userId);
        boolean root = list.contains("ROOT");
        boolean admin = list.contains("ADMIN");
        if (list != null && root && admin) {
            try {
                Object proceed = joinPoint.proceed();
                return proceed;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return AzResult.err("权限不足");
    }

    public String getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Payload<Admin> info = JwtUtils.getInfoFromToken(token, rsaKeyConfig.getPublicKey(), Admin.class);
        String userId = (String) redisTemplate.opsForValue().get("userId" + info.getUserInfo().getId());
        return userId;
    }
}
