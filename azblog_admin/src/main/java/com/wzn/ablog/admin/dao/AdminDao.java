package com.wzn.ablog.admin.dao;

import com.wzn.ablog.common.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AdminDao extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin> {

    @Query(value = "SELECT * FROM admin WHERE username = :username " +
            "OR email = :username AND STATUS = '1'", nativeQuery = true)
    Admin findAdminByUsername(String username);

    @Query(value = "SELECT username FROM admin WHERE id = :id", nativeQuery = true)
    String findUsernameById(@Param("id") String id);

    @Query(value = "UPDATE admin SET PASSWORD = :encode WHERE id = :id", nativeQuery = true)
    @Modifying
    void resetPwdById(@Param("id") String id, @Param("encode") String encode);

    @Query(value = "SELECT role_id FROM sys_role WHERE role_name = :roleName", nativeQuery = true)
    String findRoleIdByRoleName(String roleName);

    //设置权限
    @Query(value = "INSERT INTO sys_admin_role SET id=:id,admin_id = :adminId,role_id = :roleId",
            nativeQuery = true)
    @Modifying
    void setRoles(@Param("id") String id, @Param("adminId") String adminId,
                  @Param("roleId") String roleId);

    @Query(value = "SELECT username FROM admin WHERE username = :username", nativeQuery = true)
    String findUsername(String username);

    @Query(value = "DELETE FROM sys_admin_role WHERE admin_id = :adminId", nativeQuery = true)
    @Modifying
    void deleteRolesByAdminId(String adminId);

    //根据username模糊查询
    @Query(value = "SELECT * FROM admin WHERE username LIKE CONCAT('%',:username,'%')", nativeQuery = true)
    List<Admin> findByUsernameLike(String username);

    //修改用户信息
    @Query(value = "UPDATE admin SET username = :username,sex = :sex ," +
            "email = :email,status = :status WHERE id =:id", nativeQuery = true)
    @Modifying
    void update(@Param("username") String username, @Param("sex") String sex,
                @Param("email") String email, @Param("status") String status, @Param("id") String id);

    //查询用户的权限id
    @Query(value = "SELECT role_id FROM sys_role WHERE role_id \n" +
            "IN(SELECT role_id FROM sys_admin_role WHERE admin_id = :id)", nativeQuery = true)
    List<String> findRoleIdByAdminId(String id);

    //根据用户id更新权限
    @Query(value = "UPDATE sys_admin_role SET role_id = :roleId WHERE admin_id = :adminId\n", nativeQuery = true)
    void updateRoles(@PathVariable("adminId") String adminId, @PathVariable("roleId") String roleId);

    //是否有该权限
    @Query(value = "SELECT COUNT(*) FROM sys_admin_role WHERE admin_id = :adminId \n" +
            "AND role_id = :roleId",nativeQuery = true)
    int hasRole(@PathVariable("adminId") String adminId, @PathVariable("roleId") String roleId);

    //删除权限
    @Query(value = "DELETE FROM sys_admin_role WHERE admin_id = :adminId \n" +
            "AND role_id = :roleId",nativeQuery = true)
    @Modifying
    void delRole(@PathVariable("adminId") String adminId, @PathVariable("roleId") String roleId);

    //删除用户对应的角色
    @Query(value = "DELETE FROM sys_admin_role WHERE admin_id = :adminId",nativeQuery = true)
    @Modifying
    void delAllRole(String adminId);

    //更新权限
    @Query(value = "INSERT INTO sys_admin_role (id,admin_id,role_id) VALUES(:id,:adminId,:roleId);",nativeQuery = true)
    void updateRole(@Param("id") String id,@Param("adminId") String adminId,@Param("roleId") String roleId);
}
