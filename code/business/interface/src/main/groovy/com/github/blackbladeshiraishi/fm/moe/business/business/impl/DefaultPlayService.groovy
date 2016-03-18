package com.github.blackbladeshiraishi.fm.moe.business.business.impl

import com.github.blackbladeshiraishi.fm.moe.business.business.PlayList
import com.github.blackbladeshiraishi.fm.moe.business.business.PlayService
import com.github.blackbladeshiraishi.fm.moe.business.business.Player
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

/**
 * *not* thread safe
 */
class DefaultPlayService implements PlayService {

  private final Subject<PlayService.Event, PlayService.Event> eventBus = PublishSubject.create()

  private final PlayList playList

  private final Player player

  private int location = 0

  private PlayService.State state = PlayService.State.Pausing

  DefaultPlayService(PlayList playList, Player player) {
    this.playList = playList
    this.player = player
    bindPlayList()
    bindPlayer()
    //TODO unbound
  }

  private void bindPlayList() {
    //TODO unsubscribe
    playList.eventBus().ofType(PlayList.AddSongEvent).subscribe {PlayList.AddSongEvent event ->
      int currentLocation = getLocation()
      if (event.location <= currentLocation) {
        setLocation(currentLocation + 1)
      }
    }

    playList.eventBus().ofType(PlayList.RemoveSongEvent).
        subscribe {PlayList.RemoveSongEvent event ->
          int currentLocation = getLocation()
          if (event.location < currentLocation) {
            setLocation(currentLocation - 1)
          } else if (event.location == currentLocation) {
            pause()
            if (currentLocation < playList.size()) {
              play()
            } else if (currentLocation == playList.size()) {
              //NOP
            } else {
              throw new IllegalStateException()
            }
          }
        }

    playList.eventBus().ofType(PlayList.MoveSongEvent).subscribe {PlayList.MoveSongEvent event ->
      int currentLocation = getLocation()
      if (event.oldLocation == currentLocation) {
        setLocation(event.newLocation)
      } else if (event.oldLocation < currentLocation) {
        if (event.newLocation >= currentLocation) {
          setLocation(currentLocation - 1)
        }
      } else if (event.oldLocation > currentLocation) {
        if (event.newLocation <= currentLocation) {
          setLocation(currentLocation + 1)
        }
      }
    }
  }

  private void bindPlayer() {
    player.eventBus().ofType(Player.PlayEvent).subscribe {event ->
      changeState(PlayService.State.Playing)
    }

    player.eventBus().ofType(Player.PauseEvent).subscribe {event ->
      changeState(PlayService.State.Pausing)
    }

/*    // if (event.reason == Player.Reason.STOP_COMMAND) {
//        setLocation(getLocation())
    player.setPosition(0)
  }*/
    //TODO reason
    player.eventBus().ofType(Player.ErrorEvent).subscribe{
      setLocation(getLocation() + 1)
      play()
    }
    player.eventBus().ofType(Player.PlayCompletedEvent).subscribe {
      setLocation(getLocation() + 1)
      play()
    }

    player.eventBus().ofType(Player.PreparedEvent).subscribe {event ->
      //TODO clear reason
    }
  }

  private void changeState(PlayService.State state) {
    if (this.state == state) {
      return
    }
    this.state = state

    def reason = null//TODO
    def location = this.location//TODO location
    def event
    if (state == PlayService.State.Playing) {
      event = new PlayService.PlayEvent(reason, location)
    } else if (state == PlayService.State.Pausing) {
      event = new PlayService.PauseEvent(reason, location)
    } else {
      event = new PlayService.StateChangeEvent(state, reason, location)
    }
    eventBus.onNext(event)
  }

  @Override
  PlayList getPlayList() {
    return playList
  }

  @Override
  int getLocation() {
    return location
  }

  @Override
  PlayService.State getState() {
    return state
  }

  @Override
  Object getReason() {
    return reason
  }

  @Override
  Player getPlayer() {
    return player
  }

  @Override
  Observable<PlayService.Event> eventBus() {
    return eventBus.onBackpressureDrop()
  }

  @Override
  void setLocation(int location) {
    if (this.location == location) {
      return
    }
    def oldLocation = this.location
    def playingWhenChangeLocation = state == PlayService.State.Playing
    if (playingWhenChangeLocation) {
      pause()
    }
    player.setPosition(0)
    this.location = location
    eventBus.onNext(new PlayService.LocationChangeEvent(state, null, oldLocation, location))
    if (playingWhenChangeLocation && !isPlayCompleted()) {
      play()
    }
  }

  boolean isPlayCompleted() {
    location == playList.size()
  }

  @Override
  void play() {
    if (state == PlayService.State.Playing) {
      return
    }
    if (isPlayCompleted()) {
      return
    }
    player.play(playList.get(location))
  }

  @Override
  void pause() {
    if (state == PlayService.State.Pausing) {
      return
    }
    player.pause()
  }
}
