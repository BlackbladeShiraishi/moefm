package com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import javax.annotation.Nonnull;

public class AlbumKey {

  @Nonnull
  private final Content album;

  public AlbumKey(@Nonnull Content album) {
    this.album = album;
  }

  @Nonnull
  public Content getAlbum() {
    return album;
  }

  @Override
  public int hashCode() {
    long id = album.getId();
    return (int)(id ^ (id >>> 32)); // compatible with Long.hashCode()
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    return ((AlbumKey)obj).album.getId() == album.getId();
  }

}
