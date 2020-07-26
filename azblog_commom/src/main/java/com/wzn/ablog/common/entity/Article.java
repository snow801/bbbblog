package com.wzn.ablog.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "article")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {
    @Id
    private String id;
    private String title;
    private String intro;
    private String content;
    private String username;
    private String user_id;
    private String category_id;
    private String visit_count;
    private String like_count;
    private String collect_count;
    private String comment;
    private String status;
    private Date create_time;
    @Column(name = "update_time")
    private Date updateTime;
    private String is_original;
    private String recommended;


}
