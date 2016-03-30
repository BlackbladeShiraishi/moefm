package com.github.blackbladeshiraishi.fm.moe.business.api;

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

    public final State state;
    public final Object reason;
    public final int location;

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

  class PlayEvent extends StateChangeEvent {

    public PlayEvent(Object reason, int location) {
      super(State.Playing, reason, location);
    }
  }

  class PauseEvent extends StateChangeEvent {

    public PauseEvent(Object reason, int location) {
      super(State.Pausing, reason, location);
    }
  }

  class LocationChangeEvent extends Event {

    public final int oldLocation;
    public final int newLocation;

    public LocationChangeEvent(State state, Object reason, int oldLocation, int newLocation) {
      super(state, reason, newLocation);
      this.oldLocation = oldLocation;
      this.newLocation = newLocation;
    }
  }

}
