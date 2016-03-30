package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;


import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

public class RadioAdapter implements CardViewHoler.CardViewModel {

  public final Radio source;

  public RadioAdapter(Radio source) {
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
