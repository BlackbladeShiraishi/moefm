package com.github.blackbladeshiraishi.fm.moe.business.business;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import rx.Observable;

public interface RadioService {

  Observable<Radio> hotRadios();

  /**
   * songs of radio
   */
  Observable<Song> radioSongs(Radio radio);

}
