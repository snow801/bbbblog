package com.wzn.ablog.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "category")
@Data
@Accessors(chain = true)
public class Category implements Serializable {
    @Id
    private String id;
    private String name;
    private String createTime;
    private String updateTime;
    private String status;
}
