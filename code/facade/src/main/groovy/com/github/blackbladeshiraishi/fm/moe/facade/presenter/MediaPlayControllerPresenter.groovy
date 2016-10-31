package com.github.blackbladeshiraishi.fm.moe.facade.presenter

import com.github.blackbladeshiraishi.fm.moe.business.api.PlayList
import com.github.blackbladeshiraishi.fm.moe.business.api.PlayService
import com.github.blackbladeshiraishi.fm.moe.business.api.Player
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import com.github.blackbladeshiraishi.fm.moe.facade.view.MediaPlayControllerView
import rx.Scheduler
import rx.Subscription
import rx.subscriptions.Subscriptions

import javax.annotation.Nonnull
import javax.annotation.Nullable

class MediaPlayControllerPresenter {
  @Nonnull final MediaPlayControllerView view
  @Nonnull final Scheduler uiScheduler
  @Nonnull final Scheduler.Worker uiWorker
  @Nullable private Subscription subscription

  MediaPlayControllerPresenter(
      @Nonnull final MediaPlayControllerView view, @Nonnull final Scheduler uiScheduler) {
    this.view = view
    this.uiScheduler = uiScheduler
    uiWorker = uiScheduler.createWorker()
    uiWorker.schedule{reset()}
  }

  @Override
  protected void finalize() throws Throwable {
    unbindPlayService()
    super.finalize()
  }

  void bindPlayService(@Nonnull PlayService playService) {
    uiWorker.schedule{set(playService)}
    subscribe(playService)
  }

  void unbindPlayService() {
    unsubscribe()
    uiWorker.unsubscribe()
  }

  /**
   * should be called on <strong>UI Thread</strong>
   */
  private void set(@Nonnull PlayService playService) {
    Song song = null
    if (playService.location < playService.playList.size()) {
      song = playService.playList.get(playService.location)
    }
    view.showSong(song)
    updateLocation(playService.location, playService.playList.size())
    if (playService.state == PlayService.State.Playing) {
      view.setPlayButtonState(MediaPlayControllerView.PlayButtonState.PAUSE)
    } else {
      view.setPlayButtonState(MediaPlayControllerView.PlayButtonState.PLAY)
    }
  }

  /**
   * should be called on <strong>UI Thread</strong>
   */
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

  /**
   * should be called on <strong>UI Thread</strong>
   */
  private void updateLocation(int location, int total) {
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
        playService.eventBus().ofType(PlayService.PlayEvent).observeOn(uiScheduler).subscribe{
          view.setPlayButtonState(MediaPlayControllerView.PlayButtonState.PAUSE)
        },
        playService.eventBus().ofType(PlayService.PauseEvent).observeOn(uiScheduler).subscribe{
          view.setPlayButtonState(MediaPlayControllerView.PlayButtonState.PLAY)
        },
        playService.eventBus().ofType(PlayService.LocationChangeEvent).observeOn(uiScheduler)
            .subscribe{ PlayService.Event event->
          Song locationSong = null
          if (event.location < playService.playList.size()) {
            locationSong = playService.playList.get(event.location)
          }
          view.showSong(locationSong)
          updateLocation(event.location, playService.playList.size())
        },
        playService.eventBus().ofType(PlayService.CloseEvent).observeOn(uiScheduler).subscribe{
          unbindPlayService()
        },
        // bind playService.playList events to view
        playService.playList.eventBus().ofType(PlayList.Event).observeOn(uiScheduler).subscribe{
          updateLocation(playService.location, playService.playList.size())
        },
        // bind playService.player events to view
        playService.player.eventBus().ofType(Player.TickEvent).observeOn(uiScheduler).subscribe{
          Player.Event event->
          def player = event.player
          if (!player.isClosed()) {
            view.duration = player.duration
            view.position = player.position
          }
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
        },
        view.eventBus().ofType(MediaPlayControllerView.PositionViewStartTrackingTouchEvent)
            .subscribe {playService.pause()},
        view.eventBus().ofType(MediaPlayControllerView.PositionViewStopTrackingTouchEvent)
            .subscribe {playService.play()},
        view.eventBus().ofType(MediaPlayControllerView.UserChangePositionEvent).subscribe {
          MediaPlayControllerView.UserChangePositionEvent event ->
            playService.player.position = event.position
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
