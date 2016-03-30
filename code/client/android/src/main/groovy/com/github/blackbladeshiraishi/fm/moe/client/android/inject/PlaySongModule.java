package com.github.blackbladeshiraishi.fm.moe.client.android.inject;


import com.github.blackbladeshiraishi.fm.moe.business.api.PlayList;
import com.github.blackbladeshiraishi.fm.moe.business.api.PlayService;
import com.github.blackbladeshiraishi.fm.moe.business.api.Player;
import com.github.blackbladeshiraishi.fm.moe.business.api.impl.DefaultPlayList;
import com.github.blackbladeshiraishi.fm.moe.business.api.impl.DefaultPlayService;
import com.github.blackbladeshiraishi.fm.moe.client.android.business.MediaPlayerWrapper;
import com.github.blackbladeshiraishi.fm.moe.client.android.business.MediaPlayers;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaySongModule {

  @Provides
  @PlaySongScope
  PlayList providePlayList() {
    return new DefaultPlayList();
  }

  @Provides
  @PlaySongScope
  Player providePlayer() {
    return new MediaPlayerWrapper(MediaPlayers.getMediaPlayerFactory());
  }

  @Provides
  @PlaySongScope
  PlayService providePlayService(PlayList playList, Player player) {
    return new DefaultPlayService(playList, player);
  }

}
