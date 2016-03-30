package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;

import android.view.View;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;



public class AlbumListAdapter extends BaseCardClusterViewModel {

  public final List<Album> albums;

  public AlbumListAdapter(
      @Nullable View.OnClickListener onClickTitleContainerListener,
      @Nullable String title,
      @Nullable List<Album> albums) {
    super(onClickTitleContainerListener, title);
    this.albums = albums;
  }

  @Nullable
  @Override
  public List<CardViewHoler.CardViewModel> getCardViewModels() {
    if (albums == null) {
      return null;
    }
    List<CardViewHoler.CardViewModel> result = new ArrayList<>(albums.size());
    for (Album album : albums) {
      result.add(new AlbumAdapter(album));
    }
    return result;
  }

}
