package com.github.blackbladeshiraishi.fm.moe.facade.inject;

import com.github.blackbladeshiraishi.fm.moe.business.io.event.EventBus;
import com.github.blackbladeshiraishi.fm.moe.facade.controller.Controller;
import com.github.blackbladeshiraishi.fm.moe.facade.processor.BasicUiProcessor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FacadeModule {

  @Provides
  @Singleton
  BasicUiProcessor provideBasicUiProcessor(EventBus eventBus, Controller controller) {
    return new BasicUiProcessor(eventBus, controller);
  }

}
