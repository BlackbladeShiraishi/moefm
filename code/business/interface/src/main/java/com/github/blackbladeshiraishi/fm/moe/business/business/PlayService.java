package com.github.blackbladeshiraishi.fm.moe.business.business;

import rx.Observable;

public interface PlayService {

  PlayList getPlayList();

  int getLocation();

  void setLocation(int location);

  State getState();

  Object getReason();

  Player getPlayer();

  Observable<Event> eventBus();

  void play();

  void pause();

  enum State {
    Playing,
    Pausing
  }

  class Event {

    final public State state;
    final public Object reason;
    final public int location;

    public Event(State state, Object reason, int location) {
      this.state = state;
      this.reason = reason;
      this.location = location;
    }
  }

  class StateChangeEvent extends Event {

    public StateChangeEvent(State state, Object reason, int location) {
      super(state, reason, location);
    }
  }

}
