package com.github.blackbladeshiraishi.fm.moe.domain.entity;

import java.util.Map;

public class Content {

  private long id;

  private String type;

  private String title;

  /** key -> url map */
  private Map<String, String> cover;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Map<String, String> getCover() {
    return cover;
  }

  public void setCover(Map<String, String> cover) {
    this.cover = cover;
  }

}
