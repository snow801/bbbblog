package com.wzn.ablog.common.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "link")
@Data
public class Link {
  @Id
  private String id;
  private String title;
  private String summary;
  private String url;
  private long clickCount;
  private String createTime;
  private String updateTime;
  private long status;
}
