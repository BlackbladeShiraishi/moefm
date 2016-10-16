package com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.List;

import javax.annotation.Nonnull;

import flow.ClassKey;

//TODO check is't can contain List<Content>
public class AlbumListKey extends ClassKey {

  @Nonnull
  private final List<Content> albumList;

  public AlbumListKey(@Nonnull List<Content> albumList) {
    this.albumList = albumList;
  }

  @Nonnull
  public List<Content> getAlbumList() {
    return albumList;
  }

}
