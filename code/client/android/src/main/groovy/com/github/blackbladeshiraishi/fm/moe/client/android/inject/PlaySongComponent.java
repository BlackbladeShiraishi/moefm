package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.api.PlayList;
import com.github.blackbladeshiraishi.fm.moe.business.api.PlayService;

import dagger.Subcomponent;

@PlaySongScope
@Subcomponent(modules = PlaySongModule.class)
public interface PlaySongComponent {

  PlayList getPlayList();

  PlayService getPlayService();

}
