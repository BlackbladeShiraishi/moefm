package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view

import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import com.github.blackbladeshiraishi.fm.moe.facade.view.MediaPlayControllerView
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * ViewHolder of {@link R.layout#included_media_control layout/included_media_control}
 */
class MediaPlayControllerViewHolder implements MediaPlayControllerView {

  private final Subject<MediaPlayControllerView.Event, MediaPlayControllerView.Event> eventBus =
      PublishSubject.create()

  final View rootView
  final TextView songTitle
  final ImageView playOrPause
  final ImageView skipNext
  final ImageView skipPrevious
  final SeekBar progress

  @Nullable
  Song song

  MediaPlayControllerViewHolder(View rootView) {
    this.rootView = rootView
    songTitle = rootView.findViewById(R.id.song_title) as TextView
    playOrPause = rootView.findViewById(R.id.play_or_pause) as ImageView
    skipNext = rootView.findViewById(R.id.skip_next) as ImageView
    skipPrevious = rootView.findViewById(R.id.skip_previous) as ImageView
    progress = rootView.findViewById(R.id.progress) as SeekBar
    songTitle.setSelected(true)
    skipPrevious.onClickListener = {
      eventBus.onNext(new MediaPlayControllerView.ClickSkipPreviousEvent(this, song))
    }
    skipNext.onClickListener = {
      eventBus.onNext(new MediaPlayControllerView.ClickSkipNextEvent(this, song))
    }
    progress.onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

      @Override
      void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          eventBus.onNext(new MediaPlayControllerView.UserChangePositionEvent(
              MediaPlayControllerViewHolder.this, song, progress
          ))
        }
      }

      @Override
      void onStartTrackingTouch(SeekBar seekBar) {
        eventBus.onNext(new MediaPlayControllerView.PositionViewStartTrackingTouchEvent(
            MediaPlayControllerViewHolder.this, song
        ))
      }

      @Override
      void onStopTrackingTouch(SeekBar seekBar) {
        eventBus.onNext(new MediaPlayControllerView.PositionViewStopTrackingTouchEvent(
            MediaPlayControllerViewHolder.this, song
        ))
      }
    }
  }


  @Override
  Observable<MediaPlayControllerView.Event> eventBus() {
    return eventBus.onBackpressureDrop()
  }

  @Override
  void showSong(@Nullable Song song) {
    this.song = song
    songTitle.text = song?.title
  }

  @Override
  void setDuration(int duration) {
    progress.max = duration
  }

  @Override
  void setPosition(int position) {
    progress.progress = position
  }

  @Override
  void hideSkipNextButton() {
    skipNext.visibility = View.GONE
  }

  @Override
  void showSkipNextButton() {
    skipNext.visibility = View.VISIBLE
  }

  @Override
  void hideSkipPreviousButton() {
    skipPrevious.visibility = View.GONE
  }

  @Override
  void showSkipPreviousButton() {
    skipPrevious.visibility = View.VISIBLE
  }

  @Override
  void setPlayButtonState(@Nonnull MediaPlayControllerView.PlayButtonState state) {
    switch (state) {
      case MediaPlayControllerView.PlayButtonState.PLAY:
        playOrPause.imageResource = R.drawable.ic_play_arrow_white_24dp
        playOrPause.onClickListener = {
          eventBus.onNext(new MediaPlayControllerView.ClickPlayEvent(this, song))
        }
        break
      case MediaPlayControllerView.PlayButtonState.PAUSE:
        playOrPause.imageResource = R.drawable.ic_pause_white_24dp
        playOrPause.onClickListener = {
          eventBus.onNext(new MediaPlayControllerView.ClickPauseEvent(this, song))
        }
        break
      default:
        println "unsupported state: $state"//TODO log
    }
  }
}
