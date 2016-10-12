package com.github.blackbladeshiraishi.fm.moe.business.api;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import java.util.List;

import rx.Observable;

public interface SessionService {

  Observable<MoeFmMainPage> mainPage();

  /**
   * radio list
   */
  Observable<List<Radio>> radios();

  /**
   * album list
   */
  Observable<List<Album>> albums();

}
