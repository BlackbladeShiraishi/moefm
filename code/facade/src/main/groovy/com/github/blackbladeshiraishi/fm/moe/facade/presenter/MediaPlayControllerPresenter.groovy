package com.github.blackbladeshiraishi.fm.moe.facade.presenter

import com.github.blackbladeshiraishi.fm.moe.business.business.PlayService
import com.github.blackbladeshiraishi.fm.moe.business.business.Player
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import com.github.blackbladeshiraishi.fm.moe.facade.view.MediaPlayControllerView
import rx.Subscription
import rx.subscriptions.Subscriptions

import javax.annotation.Nonnull
import javax.annotation.Nullable

class MediaPlayControllerPresenter {
  @Nonnull final MediaPlayControllerView view
  @Nullable private Subscription subscription

  MediaPlayControllerPresenter(@Nonnull MediaPlayControllerView view) {
    this.view = view
    reset()
  }

  @Override
  protected void finalize() throws Throwable {
    unbindPlayService()
    super.finalize()
  }

  void bindPlayService(@Nonnull PlayService playService) {
    set(playService)
    subscribe(playService)
  }

  void unbindPlayService() {
    unsubscribe()
    reset()
  }

  private void set(@Nonnull PlayService playService) {
    Song song = null
    if (playService.location < playService.playList.size()) {
      song = playService.playList.get(playService.location)
    }
    updateLocation(song, playService.location, playService.playList.size())
    if (playService.state == PlayService.State.Playing) {
      view.setPlayButtonState(MediaPlayControllerView.PlayButtonState.PAUSE)
    } else {
      view.setPlayButtonState(MediaPlayControllerView.PlayButtonState.PLAY)
    }
  }

  private void reset() {
    view.with {
      showSong(null)
      duration = 0
      position = 0
      hideSkipPreviousButton()
      hideSkipNextButton()
      playButtonState = MediaPlayControllerView.PlayButtonState.PLAY
    }
  }

  private void updateLocation(@Nullable Song song, int location, int total) {
    view.showSong(song)
    if (location > 0) {
      view.showSkipPreviousButton()
    } else {
      view.hideSkipPreviousButton()
    }
    if (location < total - 1) {
      view.showSkipNextButton()
    } else {
      view.hideSkipNextButton()
    }
  }

  private void subscribe(@Nonnull PlayService playService) {
    unsubscribe()
    subscription = Subscriptions.from(
        // bind playService events to view
        playService.eventBus().ofType(PlayService.PlayEvent).subscribe{
          view.setPlayButtonState(MediaPlayControllerView.PlayButtonState.PAUSE)
        },
        playService.eventBus().ofType(PlayService.PauseEvent).subscribe{
          view.setPlayButtonState(MediaPlayControllerView.PlayButtonState.PLAY)
        },
        playService.player.eventBus().ofType(Player.TickEvent).subscribe{Player.Event event->
          view.duration = event.player.duration
          view.position = event.player.position
        },
        playService.eventBus().ofType(PlayService.LocationChangeEvent).subscribe{
          PlayService.Event event->
          Song locationSong = null
          if (event.location < playService.playList.size()) {
            locationSong = playService.playList.get(event.location)
          }
          updateLocation(locationSong, event.location, playService.playList.size())
        },
        // bind view events to playService
        view.eventBus().ofType(MediaPlayControllerView.ClickSkipNextEvent).subscribe{
          if (playService.location < playService.playList.size()) {
            playService.location = playService.location + 1
          }
        },
        view.eventBus().ofType(MediaPlayControllerView.ClickSkipPreviousEvent).subscribe{
          if (playService.location > 0) {
            playService.location = playService.location - 1
          }
        },
        view.eventBus().ofType(MediaPlayControllerView.ClickPlayEvent).subscribe{
          playService.play()
        },
        view.eventBus().ofType(MediaPlayControllerView.ClickPauseEvent).subscribe{
          playService.pause()
        }
    )
  }

  private void unsubscribe() {
    if (subscription == null) {
      return
    }
    if (!subscription.unsubscribed) {
      subscription.unsubscribe()
    }
    subscription = null
  }

}
