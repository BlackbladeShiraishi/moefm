package com.github.blackbladeshiraishi.fm.moe.facade.view;

import javax.annotation.Nonnull;

import rx.Observable;

/**
 * the source of events
 * @param <E> the type of events
 */
public interface EventSource<E> {

  @Nonnull
  Observable<E> eventBus();

}
