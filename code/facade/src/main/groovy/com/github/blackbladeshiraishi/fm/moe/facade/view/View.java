package com.github.blackbladeshiraishi.fm.moe.facade.view;

import javax.annotation.Nonnull;

import rx.Observable;

public interface View {

  @Nonnull
  Observable<? extends Event> eventBus();

  class Event<T extends View> {

    public final T view;

    public Event(T view) {
      this.view = view;
    }
  }

}
