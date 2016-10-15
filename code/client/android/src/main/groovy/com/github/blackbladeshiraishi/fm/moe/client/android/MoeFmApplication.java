package com.github.blackbladeshiraishi.fm.moe.client.android;

import android.app.Application;
import android.content.Context;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.AppComponent;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.DaggerAppComponent;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.MoeFmModule;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.PlaySongComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MoeFmApplication extends Application {

  private AppComponent appComponent;

  private PlaySongComponent playSongComponent;

  public static MoeFmApplication get(Context context) {
    return (MoeFmApplication) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    init();
  }

  private void init() {
    appComponent = DaggerAppComponent.builder()
        .moeFmModule(new MoeFmModule(getString(R.string.moefm_api_key)))
        .build();
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }

  @Nonnull
  public PlaySongComponent createPlaySongComponent() {
    return playSongComponent = appComponent.newPlaySongComponent();
  }

  public void releasePlaySongComponent() {
    playSongComponent = null;
  }

  @Nullable
  public PlaySongComponent getPlaySongComponent() {
    return playSongComponent;
  }

}
