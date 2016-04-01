package com.github.blackbladeshiraishi.fm.moe.business.impl.core.api

import com.github.blackbladeshiraishi.fm.moe.business.api.PlayList
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

class DefaultPlayList implements PlayList {

  private final List<Song> songs = []

  private final Subject<PlayList.Event, PlayList.Event> eventBus = PublishSubject.create()

  @Override
  Observable<PlayList.Event> eventBus() {
    return eventBus.onBackpressureDrop()
  }

  @Override
  int size() {
    return songs.size()
  }

  @Override
  Song get(int location) {
    return songs.get(location)
  }

  @Override
  void add(int location, Song song) {
    songs.add(location, song)
    eventBus.onNext(new PlayList.AddSongEvent(location, song))
  }

  @Override
  void add(Song song) {
    add(size(), song)
  }

  @Override
  Song move(int oldLocation, int newLocation) {
    if (oldLocation == newLocation) {
      return null
    } else {
      Song moved = songs.remove(oldLocation)
      songs.add(newLocation, moved)
      eventBus.onNext(new PlayList.MoveSongEvent(oldLocation, newLocation, moved))
      return moved
    }
  }

  @Override
  Song remove(int location) {
    Song removed = songs.remove(location)
    eventBus.onNext(new PlayList.RemoveSongEvent(location, removed))
    return removed
  }
}
