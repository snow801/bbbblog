package com.wzn.azblog.Statistics.dao;

import com.wzn.ablog.common.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VisitorDao extends JpaRepository<Visitor,String> , JpaSpecificationExecutor<Visitor> {

    Visitor findByHost(String host);
}
