package com.github.blackbladeshiraishi.fm.moe.facade.view;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MediaPlayControllerView
    extends View<MediaPlayControllerView, View.Event<MediaPlayControllerView>> {

  void showSong(@Nullable Song song);

  void setDuration(int duration);

  void setPosition(int position);

  void hideSkipNextButton();

  void showSkipNextButton();

  void hideSkipPreviousButton();

  void showSkipPreviousButton();

  void setPlayButtonState(@Nonnull PlayButtonState state);

  enum PlayButtonState {
    PLAY,
    PAUSE
  }

  class Event extends View.Event<MediaPlayControllerView> {
    public final Song song;

    public Event(MediaPlayControllerView view, Song song) {
      super(view);
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
  class PositionViewStartTrackingTouchEvent extends Event {

    public PositionViewStartTrackingTouchEvent(MediaPlayControllerView view, Song song) {
      super(view, song);
    }
  }
  class PositionViewStopTrackingTouchEvent extends Event {

    public PositionViewStopTrackingTouchEvent(MediaPlayControllerView view, Song song) {
      super(view, song);
    }
  }
  class UserChangePositionEvent extends Event {

    public final int position;

    public UserChangePositionEvent(MediaPlayControllerView view, Song song, int position) {
      super(view, song);
      this.position = position;
    }
  }

}
