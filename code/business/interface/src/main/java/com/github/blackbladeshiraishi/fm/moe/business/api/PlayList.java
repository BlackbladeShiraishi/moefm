package com.github.blackbladeshiraishi.fm.moe.business.api;


import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import rx.Observable;

public interface PlayList {

  Observable<Event> eventBus();

  int size();

  Song get(int location);

  void add(int location, Song song);

  void add(Song song);

  Song move(int oldLocation, int newLocation);

  Song remove(int location);

  abstract class Event {

    public final int location;
    public final Song song;

    public Event(int location, Song song) {
      this.location = location;
      this.song = song;
    }
  }

  class AddSongEvent extends Event {

    public AddSongEvent(int location, Song song) {
      super(location, song);
    }
  }

  class MoveSongEvent extends Event {

    public final int oldLocation;
    public final int newLocation;

    public MoveSongEvent(int oldLocation, int newLocation, Song song) {
      super(newLocation, song);
      this.oldLocation = oldLocation;
      this.newLocation = newLocation;
    }
  }

  class RemoveSongEvent extends Event {

    public RemoveSongEvent(int location, Song song) {
      super(location, song);
    }
  }

}
