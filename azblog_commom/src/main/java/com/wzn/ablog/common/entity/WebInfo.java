package com.wzn.ablog.common.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "web_info")
@Data
public class WebInfo {
  @Id
  private String id;
  private String logo;
  private String name;
  private String summary;
  private String author;
  private String recordNum;
  private long status;
  private String createTime;
  private String updateTime;
}
