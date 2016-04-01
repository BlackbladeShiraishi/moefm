package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import android.content.Context;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.controller.AppController;
import com.github.blackbladeshiraishi.fm.moe.facade.controller.Controller;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

  final Context context;

  public AppModule(@Nonnull Context context) {
    this.context = context.getApplicationContext();
  }

  @Provides
  @Singleton
  Controller provideController() {
    return new AppController(context);
  }

}
