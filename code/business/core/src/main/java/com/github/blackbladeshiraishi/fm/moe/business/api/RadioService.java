package com.github.blackbladeshiraishi.fm.moe.business.api;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MainPage;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;

public interface RadioService {

  Observable<? extends MainPage> mainPage();

  Observable<Radio> hotRadios();

  /**
   * search content list
   */
  Observable<List<Content>> searchContents(@Nonnull String keyword, @Nullable String type);

  /**
   * radio list
   */
  Observable<Radio> radios();

  /**
   * songs of radio
   */
  Observable<Song> radioSongs(@Nonnull Radio radio);

  /**
   * album list
   */
  Observable<Album> albums();

  /**
   * songs of album
   */
  Observable<Song> albumSongs(@Nonnull Album album);

}
