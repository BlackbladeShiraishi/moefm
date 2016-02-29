package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

class MoeFms {

  static Retrofit newRetrofit() {
    new Retrofit.Builder()
        .baseUrl("http://moe.fm")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  static MoeFmRadioService newMoeFmRadioService(Retrofit retrofit, String apiKey) {
    new MoeFmRadioService(retrofit, apiKey)
  }

}
