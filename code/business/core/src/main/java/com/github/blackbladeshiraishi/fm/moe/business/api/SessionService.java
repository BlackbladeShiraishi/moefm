package com.github.blackbladeshiraishi.fm.moe.business.api;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.List;

import rx.Observable;

public interface SessionService {

  Observable<MoeFmMainPage> mainPage();

  /**
   * radio list
   */
  Observable<List<Content>> radios();

  /**
   * album list
   */
  Observable<List<Album>> albums();

}
