package com.wzn.ablog.common.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_menu")
public class Menu{
    @Id
    private String id;
    @Column(name = "parent_id")
    private String parentId;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "menu_icon")
    private String menuIcon;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    private String roles;
}
