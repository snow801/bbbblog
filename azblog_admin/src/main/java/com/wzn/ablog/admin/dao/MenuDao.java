package com.wzn.ablog.admin.dao;

import com.wzn.ablog.common.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuDao extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {

    @Query(value = "SELECT * FROM sys_menu WHERE roles = :roles",nativeQuery = true)
    List<Menu> findByRoles(@Param("roles") String roles);

    @Query(value = "SELECT * FROM sys_menu WHERE menu_name LIKE CONCAT('%',:menuName,'%')",nativeQuery = true)
    List<Menu> findBymenuNameLink(String menuName);
}
