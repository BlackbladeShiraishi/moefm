package com.github.blackbladeshiraishi.fm.moe.facade.view;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class EventBusHelper<T> implements EventSource<T> {

  @Nonnull
  protected final Subject<T, T> eventBus = PublishSubject.create();

  @Nonnull
  public static <T> EventBusHelper<T> create() {
    return new EventBusHelper<>();
  }

  public void nextEvent(T event) {
    eventBus.onNext(event);
  }

  @Nonnull
  @Override
  public Observable<T> eventBus() {
    return eventBus.onBackpressureDrop();
  }

}
