package com.github.blackbladeshiraishi.fm.moe.business.io.event;

import rx.Observable;

public interface EventBus {

  void fireEvent(Event event);

  Observable<? extends Event> eventStream();

}
