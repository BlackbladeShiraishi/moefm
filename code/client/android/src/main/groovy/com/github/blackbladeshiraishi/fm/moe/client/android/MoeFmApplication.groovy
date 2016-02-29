package com.github.blackbladeshiraishi.fm.moe.client.android

import android.app.Application
import android.content.Context
import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.inject.MoeFmModule

class MoeFmApplication extends Application {

  private AppComponent appComponent

  static MoeFmApplication get(Context context) {
    context.getApplicationContext() as MoeFmApplication
  }

  @Override
  void onCreate() {
    super.onCreate()
//    if (BuildConfig.DEBUG) {
//      Dagger2Metrics.enableCapturing(this);
//    }

    init();
  }

  private void init() {
    appComponent = DaggerAppComponent.builder()
        .moeFmModule(new MoeFmModule(getString(R.string.moefm_api_key)))
        .build()
  }

  AppComponent getAppComponent() {
    appComponent
  }

}
