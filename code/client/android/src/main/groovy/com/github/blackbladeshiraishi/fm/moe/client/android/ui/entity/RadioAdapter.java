package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;


import android.view.View;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.facade.view.event.ShowRadioEvent;

import java.util.Map;

import javax.annotation.Nullable;

public class RadioAdapter implements CardViewHoler.CardViewModel {

  private static final String[] COVER_KEY = new String[]{"square", "small", "medium", "large"};

  public final Radio source;

  public RadioAdapter(Radio source) {
    this.source = source;
  }

  @Override
  @Nullable
  public View.OnClickListener getOnClickCardViewListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MoeFmApplication.get(v.getContext()).getAppComponent().getEventBus().fireEvent(
            new ShowRadioEvent(v, source)
        );
      }
    };
  }

  @Override
  @Nullable
  public String getTitle() {
    return source.getTitle();
  }

  @Override
  @Nullable
  public String getThumbPath() {
    return selectCover(source.getCover());
  }

  private static String selectCover(Map<String, String> cover) {
    String result = null;
    for (String key : COVER_KEY) {
      result = cover.get(key);
      if (result != null) {
        break;
      }
    }
    return result;
  }

}
