package com.github.blackbladeshiraishi.fm.moe.business.impl.core.api;

import com.github.blackbladeshiraishi.fm.moe.business.api.RadioService;
import com.github.blackbladeshiraishi.fm.moe.business.api.SessionService;
import com.github.blackbladeshiraishi.fm.moe.business.api.ThreadService;
import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.business.impl.core.utils.SkipCompleteRxOperator;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

//TODO thread on public methods
@Singleton
public class MemorySessionService implements SessionService {

  private final RadioService radioService;
  private final ThreadService threadService;

  private final BehaviorSubject<MoeFmMainPage> mainPageSubject = BehaviorSubject.create();
  private final BehaviorSubject<List<Album>> albumsSubject = BehaviorSubject.create();
  private final BehaviorSubject<List<Content>> radiosSubject = BehaviorSubject.create();

  @Inject
  public MemorySessionService(RadioService radioService, ThreadService threadService) {
    this.radioService = radioService;
    this.threadService = threadService;
  }

  @Override
  public Observable<MoeFmMainPage> mainPage() {
    if (!mainPageSubject.hasValue()) {
      loadMainPage();
    }
    return mainPageSubject.asObservable();
  }

  @Override
  public Observable<List<Album>> albums() {
    if (!albumsSubject.hasValue()) {
      loadAlbums();
    }
    return albumsSubject.asObservable();
  }

  @Override
  public Observable<List<Content>> radios() {
    if (!radiosSubject.hasValue()) {
      loadRadios();
    }
    return radiosSubject.asObservable();
  }

  private void loadMainPage() {
    radioService.mainPage()
        .cast(MoeFmMainPage.class) //TODO
        .lift(new SkipCompleteRxOperator<MoeFmMainPage>())
        .observeOn(threadService.getSingleThreadScheduler())
        .subscribeOn(Schedulers.io())
        .subscribe(mainPageSubject);
  }

  private void loadAlbums() {
    radioService.albums()
        .toList()
        .lift(new SkipCompleteRxOperator<List<Album>>())
        .observeOn(threadService.getSingleThreadScheduler())
        .subscribeOn(Schedulers.io())
        .subscribe(albumsSubject);
  }

  private void loadRadios() {
    radioService.radios()
        .toList()
        .lift(new SkipCompleteRxOperator<List<Content>>())
        .observeOn(threadService.getSingleThreadScheduler())
        .subscribeOn(Schedulers.io())
        .subscribe(radiosSubject);
  }

}
