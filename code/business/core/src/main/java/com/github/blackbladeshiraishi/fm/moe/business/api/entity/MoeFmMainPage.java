package com.github.blackbladeshiraishi.fm.moe.business.api.entity;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import java.util.List;

public class MoeFmMainPage implements MainPage {

  private List<Radio> hotRadios;

  private List<Album> newAlbums;

  private List<Album> hotAlbums;

  private List<Album> albums;

  public List<Radio> getHotRadios() {
    return hotRadios;
  }

  public void setHotRadios(List<Radio> hotRadios) {
    this.hotRadios = hotRadios;
  }

  public List<Album> getNewAlbums() {
    return newAlbums;
  }

  public void setNewAlbums(List<Album> newAlbums) {
    this.newAlbums = newAlbums;
  }

  public List<Album> getHotAlbums() {
    return hotAlbums;
  }

  public void setHotAlbums(List<Album> hotAlbums) {
    this.hotAlbums = hotAlbums;
  }

  public List<Album> getAlbums() {
    return albums;
  }

  public void setAlbums(List<Album> albums) {
    this.albums = albums;
  }

}
