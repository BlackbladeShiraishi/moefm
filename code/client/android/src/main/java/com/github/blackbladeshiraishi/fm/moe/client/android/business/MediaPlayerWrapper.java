package com.github.blackbladeshiraishi.fm.moe.client.android.business;

import android.media.MediaPlayer;

import com.github.blackbladeshiraishi.fm.moe.business.api.Player;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

//TODO thread (tick specially)
public class MediaPlayerWrapper implements Player {

  @Nonnull
  private final Subject<Player.Event, Player.Event> eventBus = PublishSubject.create();

  @Nonnull
  final MediaPlayerFactory mediaPlayerFactory;

  @Nullable
  private MediaPlayer mediaPlayer;

  private int positionWhenUnprepared = 0;

  @Nonnull
  private InternalState1 internalState1 = InternalState1.PAUSING;
  @Nonnull
  private InternalState2 internalState2 = InternalState2.UNINITIALIZED;

  @Nullable
  private Song song;

  public MediaPlayerWrapper(@Nonnull MediaPlayerFactory mediaPlayerFactory) {
    this.mediaPlayerFactory = mediaPlayerFactory;
    init();
    bindMediaPlayer(mediaPlayerFactory.create());
  }

  @Override
  protected void finalize() throws Throwable {
    unbindMediaPlayer();
    super.finalize();
  }

  private void init() {
    eventBus.ofType(Player.PlayEvent.class).subscribe( event -> {
      Observable
          .interval(16, TimeUnit.MILLISECONDS)//TODO #1 isolate TickEvent bus and see #2
          .takeUntil(eventBus.ofType(Player.PauseEvent.class))
          .subscribe(next -> eventBus.onNext(new Player.TickEvent(this)));
    });
  }

  private void bindMediaPlayer(@Nonnull MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    mediaPlayer.setOnPreparedListener( mp ->
      prepared()
    );
    mediaPlayer.setOnCompletionListener( mp ->
      playCompleted()
    );
    mediaPlayer.setOnErrorListener((mp, what, extra) -> {
      if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
        unbindMediaPlayer();
        bindMediaPlayer(mediaPlayerFactory.create());
      }
      error(extra);
      return true;
    });
  }

  private void unbindMediaPlayer() {
    if (mediaPlayer != null) {
      mediaPlayer.setOnErrorListener(null);
      mediaPlayer.setOnCompletionListener(null);
      mediaPlayer.setOnPreparedListener(null);
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }

  @Override
  public Observable<Player.Event> eventBus() {
    //TODO #2 take advantage of backpressure and see #1
    return eventBus.onBackpressureDrop();
  }

  @Override
  @Nullable
  public Song getSong() {
    return song;
  }

  @Override
  public int getDuration() {
    if (internalState2 == InternalState2.PREPARED) {
      return mediaPlayer.getDuration();
    } else {
      return -1;
    }
  }

  @Override
  public int getPosition() {
    if (internalState2 == InternalState2.PREPARED) {
      return mediaPlayer.getCurrentPosition();
    } else {
      return positionWhenUnprepared;
    }
  }

  @Override
  public void setPosition(int position) {
    if (internalState2 == InternalState2.PREPARED) {
      mediaPlayer.seekTo(position);
    } else {
      positionWhenUnprepared = position;
    }
  }

  @Override
  public void prepare(Song song) {
    if (internalState2 == InternalState2.UNINITIALIZED || this.song != song) {
      uninitialize();
      this.song = song;
      if (song.getFiles().isEmpty()) {
        error(new IOException("song.files is empty"));
        return;
      }
      internalState2 = InternalState2.PREPARING;
      eventBus.onNext(new Player.PreparingEvent(this));
      try {
        mediaPlayer.setDataSource(song.getFiles().get(0).getUrl()); //TODO select with quality
        mediaPlayer.prepareAsync();
      } catch (IOException e) {
        error(e);
      }
    }
  }

  private void prepared() {
    if (internalState2 == InternalState2.PREPARING) {
      internalState2 = InternalState2.PREPARED;
      mediaPlayer.seekTo(Math.min(positionWhenUnprepared, mediaPlayer.getDuration()));
      eventBus.onNext(new Player.PreparedEvent(this));
      if (internalState1 == InternalState1.PLAYING) {
        mediaPlayer.start();
        eventBus.onNext(new Player.PlayEvent(this));
      }
    } else {
      new IllegalStateException().printStackTrace();
    }
  }

  @Override
  public void uninitialize() {
    if (internalState2 != InternalState2.UNINITIALIZED) {
      song = null;
      positionWhenUnprepared = getPosition();
      mediaPlayer.reset();
      internalState2 = InternalState2.UNINITIALIZED;
      eventBus.onNext(new Player.UninitializedEvent(this));
    }
  }

  private void error(Object reason) {
    uninitialize();
    pause();
    eventBus.onNext(new Player.ErrorEvent(this, reason));
  }

  @Override
  public void play() {
    if (internalState1 != InternalState1.PLAYING) {
      internalState1 = InternalState1.PLAYING;
      if (internalState2 == InternalState2.PREPARED) {
        mediaPlayer.start();
        eventBus.onNext(new Player.PlayEvent(this));
      }
    }
  }

  @Override
  public void play(Song song) {
    prepare(song);
    play();
  }

  @Override
  public void pause() {
    if (internalState1 != InternalState1.PAUSING) {
      internalState1 = InternalState1.PAUSING;
      if (internalState2 == InternalState2.PREPARED) {
        mediaPlayer.pause();
      }
      eventBus.onNext(new Player.PauseEvent(this));
    }
  }

  private void playCompleted() {
    pause();
    eventBus.onNext(new Player.PlayCompletedEvent(this));
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