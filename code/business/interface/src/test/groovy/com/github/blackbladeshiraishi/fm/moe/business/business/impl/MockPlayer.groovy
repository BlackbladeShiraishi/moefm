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
    return eventBus.onBackpressureDrop()
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
    eventBus.onNext(new Player.PositionChangedEvent(this))
  }

  @Override
  void prepare(Song song) {
    this.song = song
  }

  @Override
  void pause() {
    eventBus.onNext(new Player.PauseEvent(this))
  }

  @Override
  void play() {
    eventBus.onNext(new Player.PlayEvent(this))
  }

  @Override
  void play(Song song) {
    prepare(song)
    play()
  }

  @Override
  void uninitialize() {
    song = null
  }

  void playCompletedNow() {
    position = duration
    pause()
    counter++
    eventBus.onNext(new Player.PlayCompletedEvent(this))
  }

}
