package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.api.RadioService;
import com.github.blackbladeshiraishi.fm.moe.business.impl.core.inject.BusinessImplCoreModule;
import com.github.blackbladeshiraishi.fm.moe.business.io.event.EventBus;
import com.github.blackbladeshiraishi.fm.moe.facade.inject.FacadeModule;
import com.github.blackbladeshiraishi.fm.moe.facade.processor.BasicUiProcessor;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    BusinessImplCoreModule.class,
    FacadeModule.class,
    MoeFmModule.class,
    AppModule.class
})
public interface AppComponent {

  EventBus getEventBus();

  BasicUiProcessor getBasicUiProcessor();

  RadioService getRadioService();

  PlaySongComponent newPlaySongComponent();

}
