package com.github.blackbladeshiraishi.fm.moe.business.business.impl

import com.github.blackbladeshiraishi.fm.moe.business.business.Player
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

class MockPlayer implements Player {

  private int duration = Integer.MAX_VALUE

  private final Subject<Player.Event, Player.Event> eventBus = PublishSubject.create()

  private Song song

  private int position

  int counter

  @Override
  Observable<Player.Event> eventBus() {
    return eventBus.asObservable()
  }

  @Override
  Song getSong() {
    return song
  }

  @Override
  int getPosition() {
    return position
  }

  @Override
  int getDuration() {
    return duration
  }

  @Override
  void setPosition(int position) {
    this.position = position
    eventBus.onNext(new Player.PositionChangedEvent(song, duration, position))
  }

  @Override
  void setSong(Song song) {
    this.song = song
  }

  @Override
  void pause() {
    eventBus.onNext(new Player.PauseEvent(song, duration, position))
  }

  @Override
  void resume() {
    eventBus.onNext(new Player.ResumeEvent(song, duration, position))
  }

  @Override
  void unsetSong() {
    song = null
  }

  void playCompletedNow() {
    position = duration
    pause()
    counter++
    eventBus.onNext(new Player.StopEvent(song, duration, position, Player.Reason.PLAY_COMPlETED))
  }

}
