package com.github.blackbladeshiraishi.fm.moe.business.business;


import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import javax.annotation.Nullable;

import rx.Observable;

public interface Player {

  Observable<Event> eventBus();

  @Nullable
  Song getSong();

  void prepare(Song song);

  int getPosition();

  void setPosition(int position);

  int getDuration();

  void pause();

  void play();

  void play(Song song);

  /**
   * remove song
   */
  void uninitialize();

  enum Reason {
    PLAY_COMPlETED,
    STOP_COMMAND,
    EXCEPTION_NO_FILES
  }

  class Event {

    public final Player player;

    public Event(Player player) {
      this.player = player;
    }
  }

  class PositionChangedEvent extends Event {
    public PositionChangedEvent(Player player) {
      super(player);
    }
  }

  class PlayEvent extends Event {
    public PlayEvent(Player player) {
      super(player);
    }
  }

  //TODO TickEvent Pool
  class TickEvent extends Event {
    public TickEvent(Player player) {
      super(player);
    }
  }

  class PauseEvent extends Event {
    public PauseEvent(Player player) {
      super(player);
    }
  }

  class PreparingEvent extends Event {
    public PreparingEvent(Player player) {
      super(player);
    }
  }

  class PreparedEvent extends Event {
    public PreparedEvent(Player player) {
      super(player);
    }
  }

  class UninitializedEvent extends Event {
    public UninitializedEvent(Player player) {
      super(player);
    }
  }

  class PlayCompletedEvent extends Event {
    public PlayCompletedEvent(Player player) {
      super(player);
    }
  }
  class ErrorEvent extends Event {
    public final Object reason;

    public ErrorEvent(Player player, Object reason) {
      super(player);
      this.reason = reason;
    }
  }
}
