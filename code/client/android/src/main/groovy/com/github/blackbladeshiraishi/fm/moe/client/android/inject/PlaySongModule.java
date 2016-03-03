package com.github.blackbladeshiraishi.fm.moe.client.android.inject;


import com.github.blackbladeshiraishi.fm.moe.business.business.PlayList;
import com.github.blackbladeshiraishi.fm.moe.business.business.PlayService;
import com.github.blackbladeshiraishi.fm.moe.business.business.Player;
import com.github.blackbladeshiraishi.fm.moe.business.business.impl.DefaultPlayList;
import com.github.blackbladeshiraishi.fm.moe.business.business.impl.DefaultPlayService;
import com.github.blackbladeshiraishi.fm.moe.client.android.business.AndroidPlayer;
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
  Player provideAndroidPlayer() {
    return new AndroidPlayer(MediaPlayers.getMediaPlayerFactory());
  }

  @Provides
  @PlaySongScope
  PlayService providePlayService(PlayList playList, Player player) {
    return new DefaultPlayService(playList, player);
  }

}
