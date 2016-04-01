package com.github.blackbladeshiraishi.fm.moe.client.android

import android.app.Application
import android.content.Context
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.*

import javax.annotation.Nonnull
import javax.annotation.Nullable

class MoeFmApplication extends Application {

  private AppComponent appComponent

  private PlaySongComponent playSongComponent

  static MoeFmApplication get(Context context) {
    context.getApplicationContext() as MoeFmApplication
  }

  @Override
  void onCreate() {
    super.onCreate()

    init();
  }

  private void init() {
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .moeFmModule(new MoeFmModule(getString(R.string.moefm_api_key)))
        .build()
    appComponent.basicUiProcessor.register()
  }

  AppComponent getAppComponent() {
    appComponent
  }

  @Nonnull
  PlaySongComponent createPlaySongComponent() {
    playSongComponent = appComponent.newPlaySongComponent()
  }

  void releasePlaySongComponent() {
    playSongComponent = null
  }

  @Nullable
  PlaySongComponent getPlaySongComponent() {
    playSongComponent
  }

}
