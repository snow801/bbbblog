package com.wzn.ablog.common.entity;

import lombok.Data;

import javax.management.relation.RoleInfo;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sys_port")
@Data
public class Port {
    @Id
    private String id;
    private String port_url;
    private String url_type;
    private String description;
    private String module;
    private String create_time;

    @ManyToMany(targetEntity = Role.class,fetch=FetchType.EAGER)
    @JoinTable(name = "sys_port_role",//中间表的名称
            joinColumns = {@JoinColumn(name = "port_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")}
    )
    private List<RoleInfo> roles = new ArrayList<>();
}
