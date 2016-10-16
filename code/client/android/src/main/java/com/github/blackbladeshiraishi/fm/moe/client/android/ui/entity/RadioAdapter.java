package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;


import android.view.View;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.ContentKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.Map;

import javax.annotation.Nullable;

import flow.Flow;

public class RadioAdapter implements CardViewHoler.CardViewModel {

  private static final String[] COVER_KEY = new String[]{"square", "small", "medium", "large"};

  public final Content source;

  public RadioAdapter(Content source) {
    this.source = source;
  }

  @Override
  @Nullable
  public View.OnClickListener getOnClickCardViewListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Flow.get(v.getContext()).set(new ContentKey(source));
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
