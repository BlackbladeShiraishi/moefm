package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;
import com.github.blackbladeshiraishi.fm.moe.facade.view.BaseView;
import com.github.blackbladeshiraishi.fm.moe.facade.view.MediaPlayControllerView;
import com.github.blackbladeshiraishi.fm.moe.facade.view.View.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ViewHolder of {@link R.layout#included_media_control layout/included_media_control}
 */
public class MediaPlayControllerViewHolder
        extends BaseView<MediaPlayControllerView, Event<MediaPlayControllerView>>
        implements MediaPlayControllerView {

  private final View rootView;
  private final TextView songTitle;
  private final ImageView playOrPause;
  private final ImageView skipNext;
  private final ImageView skipPrevious;
  private final SeekBar progress;

  @Nullable
  private Song song;

  public MediaPlayControllerViewHolder(View rootView) {
    this.rootView = rootView;
    songTitle = (TextView) rootView.findViewById(R.id.song_title);
    playOrPause = (ImageView) rootView.findViewById(R.id.play_or_pause);
    skipNext = (ImageView) rootView.findViewById(R.id.skip_next);
    skipPrevious = (ImageView) rootView.findViewById(R.id.skip_previous);
    progress = (SeekBar) rootView.findViewById(R.id.progress);
    songTitle.setSelected(true);
    skipPrevious.setOnClickListener(
        view -> eventBus.onNext(new MediaPlayControllerView.ClickSkipPreviousEvent(this, song)));
    skipNext.setOnClickListener(
        view -> eventBus.onNext(new MediaPlayControllerView.ClickSkipNextEvent(this, song)));
    progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          eventBus.onNext(new MediaPlayControllerView.UserChangePositionEvent(
              MediaPlayControllerViewHolder.this, song, progress
          ));
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        eventBus.onNext(new MediaPlayControllerView.PositionViewStartTrackingTouchEvent(
            MediaPlayControllerViewHolder.this, song
        ));
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        eventBus.onNext(new MediaPlayControllerView.PositionViewStopTrackingTouchEvent(
            MediaPlayControllerViewHolder.this, song
        ));
      }
    });
  }

  @Override
  public void showSong(@Nullable Song song) {
    this.song = song;
    songTitle.setText(song == null ? null : song.getTitle());
  }

  @Override
  public void setDuration(int duration) {
    progress.setMax(duration);
  }

  @Override
  public void setPosition(int position) {
    progress.setProgress(position);
  }

  @Override
  public void hideSkipNextButton() {
    skipNext.setVisibility(View.GONE);
  }

  @Override
  public void showSkipNextButton() {
    skipNext.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideSkipPreviousButton() {
    skipPrevious.setVisibility(View.GONE);
  }

  @Override
  public void showSkipPreviousButton() {
    skipPrevious.setVisibility(View.VISIBLE);
  }

  @Override
  public void setPlayButtonState(@Nonnull MediaPlayControllerView.PlayButtonState state) {
    switch (state) {
      case PLAY:
        playOrPause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        playOrPause.setOnClickListener(
            view -> eventBus.onNext(new MediaPlayControllerView.ClickPlayEvent(this, song)));
        break;
      case PAUSE:
        playOrPause.setImageResource(R.drawable.ic_pause_white_24dp);
        playOrPause.setOnClickListener(
            view -> eventBus.onNext(new MediaPlayControllerView.ClickPauseEvent(this, song)));
        break;
      default:
        System.out.println("unsupported state: " + state); //TODO log
    }
  }
}
