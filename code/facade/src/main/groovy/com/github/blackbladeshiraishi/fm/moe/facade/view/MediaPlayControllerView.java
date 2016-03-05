package com.github.blackbladeshiraishi.fm.moe.facade.view;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;

public interface MediaPlayControllerView extends View {

  @Nonnull Observable<Event> eventBus();

  void showSong(@Nullable Song song);

  void hideSkipNextButton();

  void showSkipNextButton();

  void hideSkipPreviousButton();

  void showSkipPreviousButton();

  void setPlayButtonState(@Nonnull PlayButtonState state);

  enum PlayButtonState {
    PLAY,
    PAUSE
  }

  class Event {
    public final MediaPlayControllerView view;
    public final Song song;

    public Event(MediaPlayControllerView view, Song song) {
      this.view = view;
      this.song = song;
    }
  }

  class ClickSkipNextEvent extends Event {

    public ClickSkipNextEvent(MediaPlayControllerView view, Song song) {
      super(view, song);
    }
  }
  class ClickSkipPreviousEvent extends Event {

    public ClickSkipPreviousEvent(MediaPlayControllerView view, Song song) {
      super(view, song);
    }
  }
  class ClickPlayEvent extends Event {

    public ClickPlayEvent(MediaPlayControllerView view, Song song) {
      super(view, song);
    }
  }
  class ClickPauseEvent extends Event {

    public ClickPauseEvent(MediaPlayControllerView view, Song song) {
      super(view, song);
    }
  }

}
