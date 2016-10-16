package com.github.blackbladeshiraishi.fm.moe.domain.entity;

import java.io.Serializable;
import java.util.List;

public class Song implements Serializable {

  private static final long serialVersionUID = 1L;

  private long id;
  private String title;
  private List<File> files;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<File> getFiles() {
    return files;
  }

  public void setFiles(List<File> files) {
    this.files = files;
  }


  public static class File implements Serializable {

    private static final long serialVersionUID = 1L;

    private String quality;
    private String url;

    public String getQuality() {
      return quality;
    }

    public void setQuality(String quality) {
      this.quality = quality;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

  }

}
