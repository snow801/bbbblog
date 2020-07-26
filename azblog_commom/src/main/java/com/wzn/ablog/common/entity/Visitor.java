package com.wzn.ablog.common.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "visitor")
public class Visitor {
    @Id
    private String id;
    private String host;
    private String time;
}
