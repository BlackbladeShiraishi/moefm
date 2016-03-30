package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.api.RadioService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    MoeFmModule.class
})
public interface AppComponent {

  RadioService getRadioService();

  PlaySongComponent newPlaySongComponent();

}
