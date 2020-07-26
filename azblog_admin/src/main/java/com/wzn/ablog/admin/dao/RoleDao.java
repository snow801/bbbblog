package com.wzn.ablog.admin.dao;

import com.wzn.ablog.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    /*  根据用户id查找用户权限
        nativeQuery : 使用sql的方式查询
     */
    @Query(value = "SELECT * FROM sys_role WHERE role_id \n" +
            "IN(SELECT role_id FROM sys_admin_role WHERE admin_id = :adminId)", nativeQuery = true)
    List<Role> findRolesByAminId(@Param("adminId") String adminId);


    /*  根据PortId查找请求URL权限
        nativeQuery : 使用sql的方式查询
     */
    @Query(value = "SELECT * FROM sys_role WHERE role_id \n" +
            "IN(SELECT role_id FROM sys_port_role WHERE port_id = :portId)", nativeQuery = true)
    List<Role> findRolesByPortId(@Param("portId") String portId);

    @Query(value = "SELECT * FROM sys_role WHERE role_name LIKE CONCAT('%',:roleName,'%')", nativeQuery = true)
    List<Role> findByRoleNameLink(String roleName);

    //查询该用户所能访问的接口

    @Query(value = "SELECT \n" +
            "  p.`port_url`,\n" +
            "  p.`url_type`,\n" +
            "  p.`description`,\n" +
            "  r.`role_name` \n" +
            "FROM\n" +
            "  `sys_port` p,\n" +
            "  `sys_port_role` pr,\n" +
            "  `sys_role` r \n" +
            "WHERE p.`id` = pr.`port_id`\n" +
            "  AND r.`role_id` =  pr.`role_id` \n" +
            "  AND r.`role_id` IN \n" +
            "  (SELECT \n" +
            "    r.`role_id` \n" +
            "  FROM\n" +
            "    `admin` a,\n" +
            "    `sys_admin_role` ar,\n" +
            "    `sys_role` r \n" +
            "  WHERE a.id = ar.admin_id \n" +
            "    AND r.`role_id` = ar.`role_id` \n" +
            "    AND a.`id` = :adminId)", nativeQuery = true)
    List<Object[]> portRole(@Param("adminId") String adminId);

    //给角色添加接口访问权限
    @Query(value = "INSERT INTO sys_port_role (role_id,port_id) VALUES(:roleId,:portId)", nativeQuery = true)
    @Modifying
    void roleAddPort(@Param("roleId") String roleId, @Param("portId") String portId);

    //删除角色的接口访问权限(单个)
    @Query(value = "DELETE FROM sys_port_role WHERE role_id = :roleId AND port_id = :portId", nativeQuery = true)
    @Modifying
    void delRolePort(@Param("roleId") String roleId, @Param("portId") String portId);

    //查询出角色所拥有的接口权限
    @Query(value = "SELECT \n" +
            "  p.`port_url`,\n" +
            "  p.`url_type`,\n" +
            "  p.`description`,\n" +
            "  r.`role_name` \n" +
            "FROM\n" +
            "  `sys_port_role` pr,\n" +
            "  `sys_port` p,\n" +
            "  `sys_role` r \n" +
            "WHERE pr.`port_id` = p.`id` \n" +
            "  AND pr.`role_id` = r.role_id \n" +
            "  AND r.`role_id` = :roleId ", nativeQuery = true)
    List<Object[]> findRolePort(String roleId);

    //修改角色
    @Query(value = "UPDATE sys_role SET role_name = :roleName ,comments = :comments" +
            " WHERE role_id = :roleId", nativeQuery = true)
    @Modifying
    void update(@Param("roleName") String roleName,@Param("comments") String comments,@Param("roleId") String roleId);

    //删除角色对应的全部接口权限
    @Query(value = "DELETE FROM sys_port_role WHERE role_id = :roleId",nativeQuery = true)
    @Modifying
    void delAllRolePort(String roleId);
}
