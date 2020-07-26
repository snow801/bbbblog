package com.wzn.ablog.common.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_port_role")
@Data
public class PortRole {
    @Id
    private String id;
    private String role_id;
    private String port_id;
    private String create_time;
}
