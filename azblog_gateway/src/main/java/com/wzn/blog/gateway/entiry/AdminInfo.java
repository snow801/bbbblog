package com.wzn.blog.gateway.entiry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfo {
    private String id;
    private String username;
    private String password;
    private String avatar;
    private String sex;
    private String birthday;
    private String email;
    private String createTime;
    private String lastLoginTime;
    //账号状态 0 正常 1 锁定
    private String status;

    private List<Role> roles = new ArrayList<>();

    private String[] resRoles;



}
