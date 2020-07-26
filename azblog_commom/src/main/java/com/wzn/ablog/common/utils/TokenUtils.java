package com.wzn.ablog.common.utils;

import com.wzn.ablog.common.entity.Admin;
import com.wzn.ablog.common.entity.AdminInfo;
import com.wzn.ablog.common.entity.Role;
import com.wzn.ablog.common.vo.Payload;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
public class TokenUtils {

    //获取权限
    public static String getRoles(HttpServletRequest request,RsaKeyConfig rsaKeyConfig){
        String token = request.getHeader("Authorization");
        log.debug(token);
        Payload<AdminInfo> info = JwtUtils.getInfoFromToken(token, rsaKeyConfig.getPublicKey(), AdminInfo.class);
        List<Role> roles = info.getUserInfo().getRoles();
        String rolename = "";
        for(Role role : roles){
            rolename += role.getRole_name()+",";
        }
        return rolename.substring(0,rolename.length()-1);
    }

    //获取用户id
    public static String getUserId(HttpServletRequest request,RsaKeyConfig rsaKeyConfig){
        String token = request.getHeader("Authorization");
        log.debug(token);
        Payload<Admin> info = JwtUtils.getInfoFromToken(token, rsaKeyConfig.getPublicKey(), Admin.class);
        return info.getUserInfo().getId();
    }

    //获取用户名
    public static String getUsername(HttpServletRequest request,RsaKeyConfig rsaKeyConfig){
        String token = request.getHeader("Authorization");
        log.debug(token);
        Payload<AdminInfo> info = JwtUtils.getInfoFromToken(token, rsaKeyConfig.getPublicKey(), AdminInfo.class);
        return info.getUserInfo().getUsername();
    }
}
