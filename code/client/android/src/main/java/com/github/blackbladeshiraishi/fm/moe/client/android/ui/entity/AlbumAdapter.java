package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;


import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;

import java.util.Map;

public class AlbumAdapter implements CardViewHoler.CardViewModel {

  private static final String[] COVER_KEY = new String[]{"square", "small", "medium", "large"};

  public final Album source;

  public AlbumAdapter(Album source) {
    this.source = source;
  }

  @Override
  public String getTitle() {
    return source.getTitle();
  }

  @Override
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
