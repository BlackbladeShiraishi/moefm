package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;


import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;

public class AlbumAdapter implements CardViewHoler.CardViewModel {

  public final Album source;

  public AlbumAdapter(Album source) {
    this.source = source;
  }

  @Override
  public String getTitle() {
    return source.title;
  }

  @Override
  public int getThumb() {
    return source.thumb;
  }
}
