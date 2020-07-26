package com.wzn.azblog.search.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Article implements Serializable {
    private String id;
    private String title;
    private String intro;
    private String content;
    private String nickname;
    private String user_id;
    private String category_id;
    private String visit_count;
    private String like_count;
    private String collect_count;
    private String comment;
    private String status;
    private Date create_time;
    private Date update_time;
    private String is_original;
    private String recommended;
}
