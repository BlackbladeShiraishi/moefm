package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;

import android.view.View;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardClusterViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


public class MainPageAdapter {

  public static void updateCardClusterViewModelList(
      @Nonnull List<CardClusterViewHolder.CardClusterViewModel> cardClusterViewModelList,
      @Nonnull MainPage mainPage) {
    cardClusterViewModelList.clear();
    cardClusterViewModelList.add(hotRadios(mainPage));
    cardClusterViewModelList.add(newAlbums(mainPage));
    cardClusterViewModelList.add(hotAlbums(mainPage));
    cardClusterViewModelList.add(albums(mainPage));
  }

  public static List<CardClusterViewHolder.CardClusterViewModel> newCardClusterViewModelList(
      @Nonnull MainPage mainPage) {
    List<CardClusterViewHolder.CardClusterViewModel> result = new ArrayList<>(4);
    result.add(hotRadios(mainPage));
    result.add(newAlbums(mainPage));
    result.add(hotAlbums(mainPage));
    result.add(albums(mainPage));
    return result;
  }

  private static CardClusterViewHolder.CardClusterViewModel hotRadios(MainPage mainPage) {
    final String title = "Hot Radios";
    return new RadioListAdapter(dummyListener(title), title, mainPage.hotRadios);
  }

  private static CardClusterViewHolder.CardClusterViewModel newAlbums(MainPage mainPage) {
    final String title = "New Albums";
    return new AlbumListAdapter(dummyListener(title), title, mainPage.newAlbums);
  }

  private static CardClusterViewHolder.CardClusterViewModel hotAlbums(MainPage mainPage) {
    final String title = "Hot Albums";
    return new AlbumListAdapter(dummyListener(title), title, mainPage.hotAlbums);
  }

  private static CardClusterViewHolder.CardClusterViewModel albums(MainPage mainPage) {
    final String title = "Albums";
    return new AlbumListAdapter(dummyListener(title), title, mainPage.albums);
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
