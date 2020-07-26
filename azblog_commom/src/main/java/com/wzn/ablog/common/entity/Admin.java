package com.wzn.ablog.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "admin")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements UserDetails{
    @Id
    private String id;
    private String username;
    private String password;
    private String avatar;
    private String sex;
    private String birthday;
    private String email;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "last_login_time")
    private String lastLoginTime;
    //账号状态 0 正常 1 锁定
    private String status;

    /**
     * fetch=FetchType.EAGER 关闭懒加载
     */
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "sys_admin_role",//中间表的名称
            //中间表admin_id字段关联sys_role表的主键字段id
            joinColumns = {@JoinColumn(name = "admin_id", referencedColumnName = "id")},
            //中间表role_id的字段关联sys_user表的主键role_id
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")}
    )
    private List<Role> roles = new ArrayList<>();

    @Transient//忽略该字段的映射
    private String[] resRoles;

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthority = new ArrayList<> ();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                grantedAuthority.add(new SimpleGrantedAuthority(role.getRole_name()));
            }
        }
        return grantedAuthority;

    }



    //账号未过期
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //账号未被锁定
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return status.equals("0") ? false : true;
    }

    //密码未过期
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //账号是被删除
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
