package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;


import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;

public class RadioAdapter implements CardViewHoler.CardViewModel {

  public final Radio source;

  public RadioAdapter(Radio source) {
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
