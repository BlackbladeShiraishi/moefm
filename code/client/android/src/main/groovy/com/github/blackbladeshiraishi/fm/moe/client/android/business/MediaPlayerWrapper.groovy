package com.github.blackbladeshiraishi.fm.moe.client.android.business

import android.media.MediaPlayer
import com.github.blackbladeshiraishi.fm.moe.business.business.Player
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

import javax.annotation.Nonnull
import javax.annotation.Nullable

//TODO thread (tick specially)
class MediaPlayerWrapper implements Player {

  @Nonnull
  private final Subject<Player.Event, Player.Event> eventBus = PublishSubject.create()

  @Nonnull
  final MediaPlayerFactory mediaPlayerFactory

  @Nonnull
  private MediaPlayer mediaPlayer

  private int positionWhenUnprepared = 0

  @Nonnull
  private InternalState1 internalState1 = InternalState1.PAUSING
  @Nonnull
  private InternalState2 internalState2 = InternalState2.UNINITIALIZED

  @Nullable
  private Song song

  MediaPlayerWrapper(@Nonnull MediaPlayerFactory mediaPlayerFactory) {
    this.mediaPlayerFactory = mediaPlayerFactory
    bindMediaPlayer(mediaPlayerFactory.create())
  }

  @Override
  protected void finalize() throws Throwable {
    unbindMediaPlayer()
    super.finalize()
  }

  private void bindMediaPlayer(@Nonnull MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer
    mediaPlayer.onPreparedListener = {
      prepared()
    }
    mediaPlayer.onCompletionListener = {
      playCompleted()
    }
    mediaPlayer.onErrorListener = {MediaPlayer mp, int what, int extra ->
      if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
        unbindMediaPlayer()
        bindMediaPlayer(mediaPlayerFactory.create())
      }
      error(extra)
      return true
    }
  }

  private void unbindMediaPlayer() {
    if (mediaPlayer != null) {
      mediaPlayer.onErrorListener = null
      mediaPlayer.onCompletionListener = null
      mediaPlayer.onPreparedListener = null
      mediaPlayer.release()
      mediaPlayer = null
    }
  }

  @Override
  Observable<Player.Event> eventBus() {
    return eventBus.asObservable()
  }

  @Override
  @Nullable
  public Song getSong() {
    song
  }

  @Override
  public int getDuration() {
    if (internalState2 == InternalState2.PREPARED) {
      return mediaPlayer.duration
    } else {
      return -1
    }
  }

  @Override
  public int getPosition() {
    if (internalState2 == InternalState2.PREPARED) {
      return mediaPlayer.currentPosition
    } else {
      return positionWhenUnprepared
    }
  }

  @Override
  public void setPosition(int position) {
    if (internalState2 == InternalState2.PREPARED) {
      mediaPlayer.seekTo(position)
    } else {
      positionWhenUnprepared = position
    }
  }

  @Override
  public void prepare(Song song) {
    if (internalState2 == InternalState2.UNINITIALIZED || this.song != song) {
      uninitialize()
      this.song = song
      if (song.files.isEmpty()) {
        error(new IOException('song.files is empty'))
        return
      }
      internalState2 = InternalState2.PREPARING
      eventBus.onNext(new Player.PreparingEvent(this))
      mediaPlayer.setDataSource(song.files[0].url) //TODO select with quality
      mediaPlayer.prepareAsync()
    }
  }

  private void prepared() {
    if (internalState2 == InternalState2.PREPARING) {
      internalState2 = InternalState2.PREPARED
      mediaPlayer.seekTo(Math.min(positionWhenUnprepared, mediaPlayer.duration))
      eventBus.onNext(new Player.PreparedEvent(this))
      if (internalState1 == InternalState1.PLAYING) {
        mediaPlayer.start()
        eventBus.onNext(new Player.PlayEvent(this))
      }
    } else {
      new IllegalStateException().printStackTrace()
    }
  }

  @Override
  public void uninitialize() {
    if (internalState2 != InternalState2.UNINITIALIZED) {
      song = null
      positionWhenUnprepared = getPosition()
      mediaPlayer.reset()
      internalState2 = InternalState2.UNINITIALIZED
      eventBus.onNext(new Player.UninitializedEvent(this))
    }
  }

  private void error(Object reason) {
    uninitialize()
    pause()
    eventBus.onNext(new Player.ErrorEvent(this, reason))
  }

  @Override
  public void play() {
    if (internalState1 != InternalState1.PLAYING) {
      internalState1 = InternalState1.PLAYING
      if (internalState2 == InternalState2.PREPARED) {
        mediaPlayer.start()
        eventBus.onNext(new Player.PlayEvent(this))
      }
    }
  }

  @Override
  public void play(Song song) {
    prepare(song)
    play()
  }

  @Override
  public void pause() {
    if (internalState1 != InternalState1.PAUSING) {
      internalState1 = InternalState1.PAUSING
      if (internalState2 == InternalState2.PREPARED) {
        mediaPlayer.pause()
      }
      eventBus.onNext(new Player.PauseEvent(this))
    }
  }

  private void playCompleted() {
    pause()
    eventBus.onNext(new Player.PlayCompletedEvent(this))
  }

  static enum State {

    PLAYING,
    PAUSING
  }

  private static enum InternalState1 {

    PAUSING,
    PLAYING
  }

  private static enum InternalState2 {

    UNINITIALIZED,
    PREPARING,
    PREPARED
  }

}