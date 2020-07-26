package com.wzn.azblog.base.dao;

import com.wzn.ablog.common.entity.WebInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WebInfoDao extends JpaRepository<WebInfo, String>, JpaSpecificationExecutor<WebInfo> {

    @Query(value = "UPDATE web_info SET summary = :content",nativeQuery = true)
    @Modifying
    void updateInfo(@Param("content") String content);
}
