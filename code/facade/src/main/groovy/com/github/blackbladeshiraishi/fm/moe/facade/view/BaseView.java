package com.github.blackbladeshiraishi.fm.moe.facade.view;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public abstract class BaseView<V extends View, E extends View.Event<V>> implements View<V, E> {

  @Nonnull
  protected final Subject<E, E> eventBus = PublishSubject.create();

  @Nonnull
  @Override
  public Observable<E> eventBus() {
    return eventBus.onBackpressureDrop();
  }

}
