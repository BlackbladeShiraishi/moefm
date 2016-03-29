package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;

import java.util.ArrayList;
import java.util.List;


public class Mocks {

  public static List<Radio> radios(int n) {
    List<Radio> result = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      Radio radio = new Radio();
      radio.title = "Radio " + n;
      radio.thumb = R.drawable.tbd;
      result.add(radio);
    }
    return result;
  }

  public static List<Album> albums(int n) {
    List<Album> result = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      Album album = new Album();
      album.title = "Album " + n;
      album.thumb = R.drawable.tbd;
      result.add(album);
    }
    return result;
  }

  public static MainPage mainPage(int hotRadios, int newAlbums, int hotAlbums, int albums) {
    MainPage result = new MainPage();
    result.hotRadios = radios(hotRadios);
    result.newAlbums = albums(newAlbums);
    result.hotAlbums = albums(hotAlbums);
    result.albums = albums(albums);
    return result;
  }

}
