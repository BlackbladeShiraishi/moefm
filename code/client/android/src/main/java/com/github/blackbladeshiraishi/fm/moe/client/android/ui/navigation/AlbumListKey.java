package com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;

import java.util.List;

import javax.annotation.Nonnull;

import flow.ClassKey;

public class AlbumListKey extends ClassKey {

  @Nonnull
  private final List<Album> albumList;

  public AlbumListKey(@Nonnull List<Album> albumList) {
    this.albumList = albumList;
  }

  @Nonnull
  public List<Album> getAlbumList() {
    return albumList;
  }

}
