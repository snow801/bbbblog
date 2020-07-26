package com.wzn.ablog.admin.dao;

import com.wzn.ablog.common.entity.PortRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortRoleDao extends JpaRepository<PortRole,String>, JpaSpecificationExecutor<PortRole> {

    @Query(value = "SELECT * FROM sys_port_role WHERE port_id = :portid",nativeQuery = true)
    List<PortRole> findByPortId(@Param("portid") String portid);
}
