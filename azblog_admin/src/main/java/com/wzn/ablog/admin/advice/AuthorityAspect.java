package com.wzn.ablog.admin.advice;

import com.wzn.ablog.admin.dao.RoleDao;
import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.entity.Admin;
import com.wzn.ablog.common.utils.BlogUtils;
import com.wzn.ablog.common.utils.JwtUtils;
import com.wzn.ablog.common.utils.RsaKeyConfig;
import com.wzn.ablog.common.vo.Payload;
import com.wzn.ablog.common.vo.PortRoleData;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
@Slf4j
public class AuthorityAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RsaKeyConfig rsaKeyConfig;

    @Autowired
    private RoleDao roleDao;

    @Pointcut("execution(* com.wzn.ablog.admin.web.controller.*.*(..))")
    public void pointCut() {
    }

    @Pointcut("@annotation(com.wzn.ablog.common.annotation.Authorized)")
    public void authorized() {
    }

    /**
     * 动态授权思路：
     *      1.通过用户表、角色表、接口表以及两个中间表，根据token中的用户id
     *      查询出该用户所拥有的接口访问权限，存在list集合。
     *
     *      2.使用springAOP对controller的接口进行校验，根据joinPoint拿到
     *      各种请求类型的接口url，存在一个名list集合
     *
     *      3.通过上面两个集合判断请求url是否包含该用户的接口权限，包含有
     *      就放行，没有就抛出权限不足异常
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCut() && authorized()")
    public Object GrantedAuthorityRoot(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = getUserId(request);
        //保存接口请求url
        List<String> urls = new ArrayList<>();
        //获取注解里的请求url
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Annotation[] annotations = signature.getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(signature.getMethod().getDeclaringClass(), RequestMapping.class);
            String[] MappingValue = requestMapping.value();
            if (annotation instanceof GetMapping) {
                String[] values = ((GetMapping) annotation).value();
                if (values.length > 0) {
                    for (String value : values) {
                        String portUrl = MappingValue[0] + value;
                        String urlType = "get";
                        String url = portUrl + " -" + urlType;
                        log.debug("请求接口：" + portUrl);
                        urls.add(url);
                    }
                }else {
                    String portUrl = MappingValue[0];
                    String urlType = "get";
                    String url = portUrl + " -" + urlType;
                    urls.add(url);
                }
            }

            if (annotation instanceof PostMapping) {
                String[] values = ((PostMapping) annotation).value();
                if (values.length > 0) {
                    for (String value : values) {
                        String portUrl = MappingValue[0] + value;
                        String urlType = "post";
                        String url = portUrl + " -" + urlType;
                        log.debug("请求接口：" + portUrl);
                        urls.add(url);
                    }
                }else {
                    String portUrl = MappingValue[0];
                    String urlType = "post";
                    String url = portUrl + " -" + urlType;
                    log.debug("请求接口：" + portUrl);
                    urls.add(url);
                }
            }

            if (annotation instanceof PutMapping) {
                String[] values = ((PutMapping) annotation).value();
                if (values.length > 0) {
                    for (String value : values) {
                        String portUrl = MappingValue[0] + value;
                        String urlType = "put";
                        String url = portUrl + " -" + urlType;
                        log.debug("请求接口：" + portUrl);
                        urls.add(url);
                    }
                }else {
                    String portUrl = MappingValue[0];
                    String urlType = "put";
                    String url = portUrl + " -" + urlType;
                    urls.add(url);
                }
            }

            if (annotation instanceof DeleteMapping) {
                String[] values = ((DeleteMapping) annotation).value();
                if (values.length > 0) {
                    for (String value : values) {
                        String portUrl = MappingValue[0] + value;
                        String urlType = "delete";
                        String url = portUrl + " -" + urlType;
                        log.debug("请求接口：" + portUrl);
                        urls.add(url);
                    }
                }else {
                    String portUrl = MappingValue[0];
                    String urlType = "delete";
                    String url = portUrl + " -" + urlType;
                    urls.add(url);
                }
            }
        }
        //查询出该用户所拥有的接口访问权限
        List<String> portRole = getPortRole(userId);
        //判断请求url是否包含该用户的接口权限
        for (String port : portRole) {
            //包含有就放行
            if (urls.contains(port)) {
                return joinPoint.proceed();
            }
        }
        //没有权限抛异常
        throw new RuntimeException(AzContants.PERMISSION_DENIED);
    }

    //获取token中的用户id
    public String getUserId(HttpServletRequest request) {
        String token = request.getHeader(AzContants.TOKEN_HEAD);
        Payload<Admin> info = JwtUtils.getInfoFromToken(token, rsaKeyConfig.getPublicKey(), Admin.class);
        String userId = (String) redisTemplate.opsForValue().get("userId" + info.getUserInfo().getId());
        return userId;
    }

    //获取表中的接口权限
    public List<String> getPortRole(String userId) {
        List<Object[]> objects = roleDao.portRole(userId);
        List<PortRoleData> portRoleDataList = BlogUtils.castEntity(objects, PortRoleData.class);
        List<String> portRole = new ArrayList<>();
        for (PortRoleData portRoleData : portRoleDataList) {
            String portUrl = portRoleData.getPort_url();
            String urlType = portRoleData.getUrl_type();
            String role = portUrl + " -" + urlType;
            portRole.add(role);
        }
        return portRole;
    }


}
