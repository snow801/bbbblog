package com.wzn.ablog.common.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "comment")
public class Comment implements Serializable {
    @Id
    private String id;
    private String articleId;
    private String username;
    private String content;
    private String status;
    private String avatar;
    private Date createTime;
    private Date updateTime;
}
