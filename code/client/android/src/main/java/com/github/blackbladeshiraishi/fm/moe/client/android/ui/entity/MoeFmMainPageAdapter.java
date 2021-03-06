package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;

import android.view.View;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.AlbumListKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.RadioListKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.CardClusterView.CardClusterViewModel;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import flow.Flow;

public class MoeFmMainPageAdapter {

  public static void updateCardClusterViewModelList(
      @Nonnull List<CardClusterViewModel> cardClusterViewModelList,
      @Nonnull MoeFmMainPage mainPage) {
    cardClusterViewModelList.clear();
    cardClusterViewModelList.add(hotRadios(mainPage));
    cardClusterViewModelList.add(newAlbumListCardClusterModel("新曲速递", mainPage.getNewAlbums()));
    cardClusterViewModelList.add(newAlbumListCardClusterModel("音乐热榜", mainPage.getHotAlbums()));
    cardClusterViewModelList.add(newAlbumListCardClusterModel("最新音乐", mainPage.getAlbums()));
  }

  public static List<CardClusterViewModel> newCardClusterViewModelList(
      @Nonnull MoeFmMainPage mainPage) {
    List<CardClusterViewModel> result = new ArrayList<>(4);
    updateCardClusterViewModelList(result, mainPage);
    return result;
  }

  private static CardClusterViewModel hotRadios(final MoeFmMainPage mainPage) {
    final String title = "流行电台";
    final List<Content> hotRadios = mainPage.getHotRadios();
    return new RadioListAdapter(newShowRadioListOnClickListener(hotRadios), title, hotRadios);
  }
  @Nonnull
  private static View.OnClickListener newShowRadioListOnClickListener(
      @Nonnull final List<Content> radioList) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Flow.get(v).set(new RadioListKey(radioList));
      }
    };
  }

  private static CardClusterViewModel newAlbumListCardClusterModel(
      @Nonnull final String title, @Nonnull final List<Content> albumList) {
    return new AlbumListAdapter(newShowAlbumListOnClickListener(albumList), title, albumList);
  }

  @Nonnull
  private static View.OnClickListener newShowAlbumListOnClickListener(
      @Nonnull final List<Content> albumList) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Flow.get(v).set(new AlbumListKey(albumList));
      }
    };
  }

}
