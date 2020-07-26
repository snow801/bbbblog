package com.wzn.ablog.admin.service;

import com.wzn.ablog.admin.dao.AdminDao;
import com.wzn.ablog.admin.dao.RoleDao;
import com.wzn.ablog.common.entity.Admin;
import com.wzn.ablog.common.entity.Role;
import com.wzn.ablog.common.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private IdWorker idWorker;

    public static final String ROLE_ROOT = "ROOT";

    public static final String ROLE_ADMIN = "ADMIN";
    //登录
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admin admin = adminDao.findAdminByUsername(s);
        return admin;
    }

    //申请账号
    public void apply(Map<String, String> params) {
        Admin admin = new Admin();
        String id = idWorker.nextId() + "";
        Stream.of(params).forEach(map -> {
            admin.setId(id);
            admin.setUsername(map.get("username"));
            admin.setPassword(encoder.encode(map.get("password")));
            admin.setSex("男");
            admin.setEmail(map.get("email"));
            admin.setStatus("1");
        });
        adminDao.save(admin);
        adminDao.setRoles(idWorker.nextId() + "", id, "2");
    }

    //根据id查找用户名
    public String getUsernameById(String id) {
        return adminDao.findUsernameById(id);
    }

    //加载用户列表
    public Page<Admin> getAdminList(int page, int limit) {
        Page<Admin> pageInfo = adminDao.findAll(PageRequest.of(page - 1, limit, Sort.by("createTime").descending()));
        return pageInfo;
    }

    //重置密码
    public void resetPwd(String id) {
        String encode = encoder.encode("123456");
        adminDao.resetPwdById(id, encode);
    }

    //根据id查找用户
    public Admin findById(String id) {
        Admin admin = adminDao.findById(id).get();
        if (admin.getUsername().equals("root")) {
            return null;
        }
        return adminDao.findById(id).get();
    }

    //添加用户并设置权限
    public void add(Admin admin) {
        admin.setId(String.valueOf(idWorker.nextId()));
        admin.setStatus(admin.getStatus() != null ?
                admin.getStatus().equals("on") ? "0" : "1" : "1");
        admin.setPassword(encoder.encode(admin.getPassword()));
        Admin save = adminDao.save(admin);
        String[] reqRoles = admin.getResRoles();
        for (String role : reqRoles) {
            String roleName = adminDao.findRoleIdByRoleName(role);
            adminDao.setRoles(String.valueOf(idWorker.nextId()), save.getId(), roleName);
        }
    }

    //用户名是否重复
    public boolean adminIsRepetitive(String username) {
        String name = adminDao.findUsername(username);
        if (name != null) {
            return true;
        }
        return false;
    }

    //根据id删除
    public boolean delete(String id) {
        Admin admin = adminDao.findById(id).get();
        if (admin.getUsername().equals("root")) {
            return false;
        } else {
            adminDao.deleteRolesByAdminId(id);
            adminDao.deleteById(id);
            return true;
        }
    }

    //根据用户名搜索
    public Page<Admin> searchByUsername(String keyword, int page, int limit) {
        Specification<Admin> specification = new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //要模糊查询的字段
                Path username = root.get("username");
                //criteriaBuilder.like模糊查询，第一个参数是上一行的返回值，第二个参数是like('%xxx%')中，xxx的值
                Predicate predicate = criteriaBuilder.like(username, "%" + keyword + "%");
                return predicate;
            }
        };
        Page<Admin> admins = adminDao.findAll(specification, PageRequest.of(page - 1, limit));
        return admins;
    }

    //修改用户信息
    public void update(Map params) {
        String status = (String) params.get("status");
        if (status == null) {
            status = "0";
        }
        //修改用户信息
        adminDao.update((String) params.get("username"), (String) params.get("sex"), (String) params.get("mail"),
                status.equals("on") ? "1" : "0", (String) params.get("id"));

        //修改权限
        String adminId = params.get("id")+"";
        String roleId = params.get("roleId")+"";
        adminDao.delAllRole(adminId);
        adminDao.setRoles(idWorker.nextId()+"",adminId,roleId);

    }

    public void refreshPort(){
        File file = new File("com.wzn.ablog.admin.web.controller");

    }
}
