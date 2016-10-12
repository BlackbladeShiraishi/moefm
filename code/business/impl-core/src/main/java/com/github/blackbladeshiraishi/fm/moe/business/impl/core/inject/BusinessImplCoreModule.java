package com.github.blackbladeshiraishi.fm.moe.business.impl.core.inject;

import com.github.blackbladeshiraishi.fm.moe.business.api.SessionService;
import com.github.blackbladeshiraishi.fm.moe.business.impl.core.api.MemorySessionService;
import com.github.blackbladeshiraishi.fm.moe.business.impl.core.io.event.PublishEventBus;
import com.github.blackbladeshiraishi.fm.moe.business.io.event.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BusinessImplCoreModule {

  @Provides
  @Singleton
  EventBus provideEventBus() {
    return new PublishEventBus();
  }

  @Provides
  @Singleton
  SessionService provideSessionService(MemorySessionService sessionService) {
    return sessionService;
  }

}
