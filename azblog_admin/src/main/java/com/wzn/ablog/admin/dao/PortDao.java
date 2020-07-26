package com.wzn.ablog.admin.dao;

import com.wzn.ablog.common.entity.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PortDao extends JpaRepository<Port,String>, JpaSpecificationExecutor<Port> {


}
