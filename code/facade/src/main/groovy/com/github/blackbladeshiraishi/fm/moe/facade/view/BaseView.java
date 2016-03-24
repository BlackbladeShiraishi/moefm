package com.github.blackbladeshiraishi.fm.moe.facade.view;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public abstract class BaseView<T extends View> implements View {

  @Nonnull
  protected final Subject<Event<T>, Event<T>> eventBus = PublishSubject.create();

  @Nonnull
  @Override
  public Observable<? extends Event> eventBus() {
    return eventBus.onBackpressureDrop();
  }

}
