package com.wzn.ablog.article.dao;

import com.wzn.ablog.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.plaf.TreeUI;

public interface CategoryDao extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {

    @Query(value = "SELECT NAME FROM `category` WHERE id=:id", nativeQuery = true)
    String findNameById(String id);

    @Query(value = "UPDATE `category` SET NAME = :name WHERE id = :id", nativeQuery = true)
    @Modifying
    void update(@Param("name") String name, @Param("id") String id);
}
