package com.github.blackbladeshiraishi.fm.moe.business.api;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MainPage;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.User;

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
  Observable<Content> radios();

  /**
   * songs of radio
   */
  Observable<Song> radioSongs(long radioId);

  /**
   * album list
   */
  Observable<Album> albums();

  /**
   * songs of album
   */
  Observable<Song> albumSongs(@Nonnull Album album);

  /**
   * detail of user
   */
  Observable<User> user(String uid);

}
