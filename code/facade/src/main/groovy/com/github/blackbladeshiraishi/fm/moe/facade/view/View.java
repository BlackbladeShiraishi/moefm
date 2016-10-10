package com.github.blackbladeshiraishi.fm.moe.facade.view;

/**
 * the interface
 * @param <V> the type of the implement class of {@link View}
 * @param <E> the type of events emitted by {@link V}
 */
public interface View<V extends View, E extends View.Event<V>> extends EventSource<E> {

  class Event<T extends View> {

    public final T view;

    public Event(T view) {
      this.view = view;
    }
  }

}
