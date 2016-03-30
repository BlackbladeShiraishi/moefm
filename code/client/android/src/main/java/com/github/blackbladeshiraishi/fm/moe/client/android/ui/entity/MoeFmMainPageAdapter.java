package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;

import android.view.View;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardClusterViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


public class MoeFmMainPageAdapter {

  public static void updateCardClusterViewModelList(
      @Nonnull List<CardClusterViewHolder.CardClusterViewModel> cardClusterViewModelList,
      @Nonnull MoeFmMainPage mainPage) {
    cardClusterViewModelList.clear();
    cardClusterViewModelList.add(hotRadios(mainPage));
    cardClusterViewModelList.add(newAlbums(mainPage));
    cardClusterViewModelList.add(hotAlbums(mainPage));
    cardClusterViewModelList.add(albums(mainPage));
  }

  public static List<CardClusterViewHolder.CardClusterViewModel> newCardClusterViewModelList(
      @Nonnull MoeFmMainPage mainPage) {
    List<CardClusterViewHolder.CardClusterViewModel> result = new ArrayList<>(4);
    result.add(hotRadios(mainPage));
    result.add(newAlbums(mainPage));
    result.add(hotAlbums(mainPage));
    result.add(albums(mainPage));
    return result;
  }

  private static CardClusterViewHolder.CardClusterViewModel hotRadios(MoeFmMainPage mainPage) {
    final String title = "Hot Radios";
    return new RadioListAdapter(dummyListener(title), title, mainPage.getHotRadios());
  }

  private static CardClusterViewHolder.CardClusterViewModel newAlbums(MoeFmMainPage mainPage) {
    final String title = "New Albums";
    return new AlbumListAdapter(dummyListener(title), title, mainPage.getNewAlbums());
  }

  private static CardClusterViewHolder.CardClusterViewModel hotAlbums(MoeFmMainPage mainPage) {
    final String title = "Hot Albums";
    return new AlbumListAdapter(dummyListener(title), title, mainPage.getHotAlbums());
  }

  private static CardClusterViewHolder.CardClusterViewModel albums(MoeFmMainPage mainPage) {
    final String title = "Albums";
    return new AlbumListAdapter(dummyListener(title), title, mainPage.getAlbums());
  }

  private static View.OnClickListener dummyListener(final String title) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked " + title, Toast.LENGTH_LONG).show();
      }
    };
  }

}
