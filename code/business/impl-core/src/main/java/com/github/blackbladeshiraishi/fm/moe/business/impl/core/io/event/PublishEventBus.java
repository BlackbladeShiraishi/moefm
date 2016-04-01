package com.github.blackbladeshiraishi.fm.moe.business.impl.core.io.event;

import com.github.blackbladeshiraishi.fm.moe.business.io.event.Event;
import com.github.blackbladeshiraishi.fm.moe.business.io.event.EventBus;

import javax.annotation.concurrent.NotThreadSafe;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

@NotThreadSafe
public class PublishEventBus implements EventBus {

  private final Subject<Event, Event> subject = PublishSubject.create();

  @Override
  public void fireEvent(Event event) {
    subject.onNext(event);
  }

  @Override
  public Observable<? extends Event> eventStream() {
    return subject.onBackpressureDrop();
  }

}
