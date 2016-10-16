package com.github.blackbladeshiraishi.fm.moe.business.api.entity;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.List;

public class MoeFmMainPage implements MainPage {

  private List<Content> hotRadios;

  private List<Content> newAlbums;

  private List<Content> hotAlbums;

  private List<Content> albums;

  public List<Content> getHotRadios() {
    return hotRadios;
  }

  public void setHotRadios(List<Content> hotRadios) {
    this.hotRadios = hotRadios;
  }

  public List<Content> getNewAlbums() {
    return newAlbums;
  }

  public void setNewAlbums(List<Content> newAlbums) {
    this.newAlbums = newAlbums;
  }

  public List<Content> getHotAlbums() {
    return hotAlbums;
  }

  public void setHotAlbums(List<Content> hotAlbums) {
    this.hotAlbums = hotAlbums;
  }

  public List<Content> getAlbums() {
    return albums;
  }

  public void setAlbums(List<Content> albums) {
    this.albums = albums;
  }

}
