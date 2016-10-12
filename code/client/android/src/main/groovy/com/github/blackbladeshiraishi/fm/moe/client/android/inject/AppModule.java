package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.api.ThreadService;
import com.github.blackbladeshiraishi.fm.moe.client.android.service.AndroidThreadService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

  @Singleton
  @Provides
  ThreadService proviThreadService(AndroidThreadService threadService) {
    return threadService;
  }

}
