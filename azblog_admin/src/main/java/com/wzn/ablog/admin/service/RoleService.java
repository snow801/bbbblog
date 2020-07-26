package com.wzn.ablog.admin.service;

import com.wzn.ablog.admin.dao.PortDao;
import com.wzn.ablog.admin.dao.RoleDao;
import com.wzn.ablog.common.entity.Port;
import com.wzn.ablog.common.entity.Role;
import com.wzn.ablog.common.utils.BlogUtils;
import com.wzn.ablog.common.utils.IdWorker;
import com.wzn.ablog.common.vo.PortRoleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
@Transactional
public class RoleService {

    @Autowired
    private PortDao portDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;

    //查找全部接口
    public List<Map<String, Object>> portList(String roleId) {
        List<Port> ports = portDao.findAll();
        //查询出所有的接口
        List<Map<String, Object>> maps = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (Port port : ports) {
            String formatPort = BlogUtils.formatPort(port.getPort_url(), port.getUrl_type());
            urls.add(formatPort);
        }
        //查询出该角色所拥有的接口访问权限
        List<Object[]> objects = roleDao.findRolePort(roleId);
        List<PortRoleData> portRoleDataList = BlogUtils.castEntity(objects, PortRoleData.class);

        //判断是否有权限
        for (Port port : ports) {
            Map<String, Object> map = BlogUtils.objectToMap(port);
            map.put("checked", "false");
            for (PortRoleData portRoleData : portRoleDataList) {
                if (port.getPort_url().equals(portRoleData.getPort_url())
                        && port.getUrl_type().equals(portRoleData.getUrl_type())) {
                    map.put("checked", "true");//有权限就设置checked为true
                }
            }
            maps.add(map);
        }
        return maps;
    }

    //查找全部权限
    public Page<Role> roleList(int page, int limit) {
        Page<Role> rolePage = roleDao.findAll(PageRequest.of(page - 1, limit));
        return rolePage;
    }

    //根据id查找权限
    public Role findById(String id) {
        Role role = roleDao.findById(id).get();
        if(role.getRole_name().equals("ROOT")){
            return null;
        }
        return role;
    }

    //添加
    public void add(Role role) {
        role.setRole_id(idWorker.nextId() + "");
        roleDao.save(role);
    }

    //更新
    public void update(Role role) {
        Role r = roleDao.findById(role.getRole_id()).get();
        roleDao.update(role.getRole_name(),role.getComments(),role.getRole_id());
    }

    //根据id删除
    public boolean del(String[] ids) {
        for (String id : ids) {
            Role role = roleDao.findById(id).get();
            if(role.getRole_name().equals("ROOT")){
                return false;
            }
            roleDao.deleteById(id);
            roleDao.delAllRolePort(id);
        }
        return true;
    }

    //根据角色名搜索
    public Page<Role>  searchRole(String roleName, int page, int limit) {
        Specification<Role> specification = new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path role_name = root.get("role_name");
                Predicate predicate = criteriaBuilder.like(role_name, "%" + roleName + "%");
                return predicate;
            }
        };
        Page<Role> rolePage = roleDao.findAll(specification, PageRequest.of(page - 1, limit));
        return rolePage;
    }

    //给角色添加接口访问权限
    public void roleAddPort(String roleId, String portId,String checked) {
       //授权
        if(checked.equals("true")){
            roleDao.roleAddPort(roleId,portId);
        }
        //删除授权
        if(checked.equals("false")){
            roleDao.delRolePort(roleId,portId);
        }

    }

    //角色列表，不分页
    public List<Role> allRole() {
        List<Role> list = roleDao.findAll();
        return list;
    }
}
