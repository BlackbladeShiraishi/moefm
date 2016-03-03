package com.github.blackbladeshiraishi.fm.moe.business.business;


import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import javax.annotation.Nullable;

import rx.Observable;

public interface Player {

  Observable<Event> eventBus();

  @Nullable
  Song getSong();

  void setSong(Song song);

  int getPosition();

  void setPosition(int position);

  int getDuration();

  void pause();

  void resume();

  /**
   * remove song
   */
  void unsetSong();

  enum State {
    PLAYING,
    PAUSING
  }

  enum Reason {
    PLAY_COMPlETED,
    STOP_COMMAND,
    EXCEPTION_NO_FILES
  }

  class Event {

    public final Song song;
    public final int duration;
    public final int position;

    public Event(Song song, int duration, int position) {
      this.song = song;
      this.duration = duration;
      this.position = position;
    }
  }

  class PositionChangedEvent extends Event {

    public PositionChangedEvent(Song song, int duration, int position) {
      super(song, duration, position);
    }
  }

  class StartEvent extends Event {

    public StartEvent(Song song, int duration, int position) {
      super(song, duration, position);
    }
  }

  class ResumeEvent extends Event {

    public ResumeEvent(Song song, int duration, int position) {
      super(song, duration, position);
    }
  }

  //TODO TickEvent Pool
  class TickEvent extends Event {

    public TickEvent(Song song, int duration, int position) {
      super(song, duration, position);
    }
  }

  class PauseEvent extends Event {

    public PauseEvent(Song song, int duration, int position) {
      super(song, duration, position);
    }
  }

  class StopEvent extends Event {

    public final Object reason;

    public StopEvent(Song song, int duration, int position, Object reason) {
      super(song, duration, position);
      this.reason = reason;
    }
  }
}
