package com.github.blackbladeshiraishi.fm.moe.client.android.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.facade.controller.Controller

class ControllerService extends Service {

  private final IBinder binder = new LocalBinder()

  Controller controller

  @Override
  void onCreate() {
    super.onCreate()
    controller = new Controller(getString(R.string.moefm_api_key))
  }

  @Override
  IBinder onBind(Intent intent) {
    return binder
  }

  @Override
  void onDestroy() {
    controller = null
    super.onDestroy()
  }

  class LocalBinder extends Binder {

    ControllerService getService() {
      // Return this instance of LocalService so clients can call public methods
      return ControllerService.this;
    }
  }

}
