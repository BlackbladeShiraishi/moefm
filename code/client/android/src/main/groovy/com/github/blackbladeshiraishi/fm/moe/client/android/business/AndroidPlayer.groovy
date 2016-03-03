package com.github.blackbladeshiraishi.fm.moe.client.android.business

import android.media.MediaPlayer
import com.github.blackbladeshiraishi.fm.moe.business.business.Player
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

import javax.annotation.Nonnull
import java.util.concurrent.TimeUnit

class AndroidPlayer implements Player {

  private final Subject<Player.Event, Player.Event> eventBus = PublishSubject.create()

  @Nonnull
  final MediaPlayerFactory mediaPlayerFactory
  @Nonnull
  MediaPlayer mediaPlayer

  private Song song

  private int position

  private int duration

  private Player.State state = Player.State.PAUSING

  AndroidPlayer(@Nonnull MediaPlayerFactory mediaPlayerFactory) {
    init()
    this.mediaPlayerFactory = mediaPlayerFactory
    bindMediaPlayer(mediaPlayerFactory.create())

  }

  private init() {
    eventBus.ofType(Player.ResumeEvent).subscribe{
      state = Player.State.PLAYING
    }
    eventBus.ofType(Player.PauseEvent).subscribe{
      state = Player.State.PAUSING
    }
    eventBus.ofType(Player.TickEvent).subscribe{Player.TickEvent event ->
      position = event.position
      duration = event.duration
    }
  }

  private void bindMediaPlayer(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer
    mediaPlayer.setOnCompletionListener{MediaPlayer mp ->
      stop(Player.Reason.PLAY_COMPlETED)
    }
    mediaPlayer.setOnErrorListener{MediaPlayer mp, int what, int extra ->
      if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
        mediaPlayer.release()
        bindMediaPlayer(mediaPlayerFactory.create())
        return true
      }
      stop(extra)
      return true
    }
  }

  @Override
  Observable<Player.Event> eventBus() {
    return eventBus.asObservable()
  }

  @Override
  Song getSong() {
    return song
  }

  @Override
  void setSong(Song song) {
    if (this.song == song) {
      return
    }
    this.song = song
    duration = -1
    position = 0
  }

  @Override
  int getPosition() {
    return position
  }

  @Override
  void setPosition(int position) {
    //TODO
  }

  @Override
  int getDuration() {
    return duration
  }

  @Override
  void pause() {
    if (state == Player.State.PAUSING) {
      return
    }
    //TODO
    eventBus.onNext(new Player.PauseEvent(song, duration, position))
  }

  @Override
  void resume() {
    if (state == Player.State.PLAYING) {
      return
    }
    eventBus.onNext(new Player.ResumeEvent(song, duration, position))
    if (song.files.isEmpty()) {
      stop(Player.Reason.EXCEPTION_NO_FILES)
      return
    }
    mediaPlayer.reset()//TODO hasPrepared
    mediaPlayer.setDataSource(song.files[0].url) //TODO select with quality
    mediaPlayer.setOnPreparedListener{MediaPlayer mp ->//TODO state changed to pausing
      //TODO thread
      Observable.interval(16, TimeUnit.MILLISECONDS).startWith(0 as long)
          .takeUntil(pauseStream()).subscribe{
            eventBus.onNext(new Player.TickEvent(
                song, mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition()))
          }
      mp.start()
    }
    mediaPlayer.prepareAsync()
  }

  @Override
  void unsetSong() {
    song = null
    duration = -1
    position = 0
  }

  private void stop(Object reason) {
    pause()
    eventBus.onNext(new Player.StopEvent(song, duration, position, reason))
  }

  private Observable<Player.PauseEvent> pauseStream() {
    eventBus.ofType(Player.PauseEvent) as Observable<Player.PauseEvent>
  }
}
