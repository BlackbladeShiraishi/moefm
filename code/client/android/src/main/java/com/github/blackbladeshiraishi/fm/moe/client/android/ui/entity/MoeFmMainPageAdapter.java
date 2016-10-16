package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;

import android.view.View;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.AlbumListKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.RadioListKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardClusterViewHolder;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import flow.Flow;

public class MoeFmMainPageAdapter {

  public static void updateCardClusterViewModelList(
      @Nonnull List<CardClusterViewHolder.CardClusterViewModel> cardClusterViewModelList,
      @Nonnull MoeFmMainPage mainPage) {
    cardClusterViewModelList.clear();
    cardClusterViewModelList.add(hotRadios(mainPage));
    cardClusterViewModelList.add(newAlbumListCardClusterModel("新曲速递", mainPage.getNewAlbums()));
    cardClusterViewModelList.add(newAlbumListCardClusterModel("音乐热榜", mainPage.getHotAlbums()));
    cardClusterViewModelList.add(newAlbumListCardClusterModel("最新音乐", mainPage.getAlbums()));
  }

  public static List<CardClusterViewHolder.CardClusterViewModel> newCardClusterViewModelList(
      @Nonnull MoeFmMainPage mainPage) {
    List<CardClusterViewHolder.CardClusterViewModel> result = new ArrayList<>(4);
    updateCardClusterViewModelList(result, mainPage);
    return result;
  }

  private static CardClusterViewHolder.CardClusterViewModel hotRadios(final MoeFmMainPage mainPage) {
    final String title = "流行电台";
    final List<Radio> hotRadios = mainPage.getHotRadios();
    return new RadioListAdapter(newShowRadioListOnClickListener(hotRadios), title, hotRadios);
  }
  @Nonnull
  private static View.OnClickListener newShowRadioListOnClickListener(
      @Nonnull final List<Radio> radioList) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Flow.get(v).set(new RadioListKey(radioList));
      }
    };
  }

  private static CardClusterViewHolder.CardClusterViewModel newAlbumListCardClusterModel(
      @Nonnull final String title, @Nonnull final List<Album> albumList) {
    return new AlbumListAdapter(newShowAlbumListOnClickListener(albumList), title, albumList);
  }

  @Nonnull
  private static View.OnClickListener newShowAlbumListOnClickListener(
      @Nonnull final List<Album> albumList) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Flow.get(v).set(new AlbumListKey(albumList));
      }
    };
  }

}
