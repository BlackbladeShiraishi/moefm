package com.github.blackbladeshiraishi.fm.moe.client.android.business;

import android.media.MediaPlayer;

import javax.annotation.Nonnull;

public class MediaPlayers {

  private static final MediaPlayerFactory SIMPLE_FACTORY = new MediaPlayerFactory() {
    @Override
    public MediaPlayer create() {
      return new MediaPlayer();
    }
  };

  @Nonnull
  public static MediaPlayerFactory getMediaPlayerFactory() {
    return SIMPLE_FACTORY;
  }

}
