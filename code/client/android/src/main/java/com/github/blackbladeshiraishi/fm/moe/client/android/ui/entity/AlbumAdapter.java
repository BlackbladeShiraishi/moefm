package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;


import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;

public class AlbumAdapter implements CardViewHoler.CardViewModel {

  public final Album source;

  public AlbumAdapter(Album source) {
    this.source = source;
  }

  @Override
  public String getTitle() {
    return source.getTitle();
  }

  @Override
  public int getThumb() {
    return R.drawable.tbd;
  }
}
