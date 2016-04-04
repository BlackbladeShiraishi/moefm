package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.api.RadioService;
import com.github.blackbladeshiraishi.fm.moe.business.impl.core.inject.BusinessImplCoreModule;
import com.github.blackbladeshiraishi.fm.moe.business.io.event.EventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    BusinessImplCoreModule.class,
    MoeFmModule.class
})
public interface AppComponent {

  EventBus getEventBus();

  RadioService getRadioService();

  PlaySongComponent newPlaySongComponent();

}
