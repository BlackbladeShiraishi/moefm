package com.github.blackbladeshiraishi.fm.moe.business.api;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MainPage;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import javax.annotation.Nonnull;

import rx.Observable;

public interface RadioService {

  Observable<? extends MainPage> mainPage();

  Observable<Radio> hotRadios();

  /**
   * songs of radio
   */
  Observable<Song> radioSongs(@Nonnull Radio radio);

  /**
   * songs of album
   */
  Observable<Song> albumSongs(@Nonnull Album album);

}
