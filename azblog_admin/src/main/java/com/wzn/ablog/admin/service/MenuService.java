package com.wzn.ablog.admin.service;

import com.wzn.ablog.admin.dao.MenuDao;
import com.wzn.ablog.common.entity.Menu;
import com.wzn.ablog.common.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuDao menuDao;

    //根据权限加载菜单
    public List<Menu> listByAuth(String roles){
        String[] roleName = roles.split(",");
        List<Menu> roleList = menuDao.findByRoles(roles);
        for (int i = 0; i < roleName.length && roleName.length>1 ; i++) {
            roleList.addAll(menuDao.findByRoles(roleName[i]));
        }
        return roleList;
    }

    //查找全部菜单
    public Page<Menu> list(int page,int limit){
        Page<Menu> pageInfo = menuDao.findAll(PageRequest.of(page - 1, limit, Sort.by("updateTime").descending()));
        return pageInfo;
    }

    //根据id查找权限
    public Menu findById(String id){
        Menu menu = menuDao.findById(id).get();
        return menu;
    }

    //添加
    public void add(Menu menu){
        menuDao.save(menu);
    }

    //更新
    public void update(Menu menu){
        menuDao.save(menu);
    }

    //根据id删除
    public void del(String[] ids){
        for(String id : ids){
            menuDao.deleteById(id);
        }
    }

    //根据菜单名搜索
    public List<Menu> searchMenu(String menuName){
        return menuDao.findBymenuNameLink(menuName);
    }
}
