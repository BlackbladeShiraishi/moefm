package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

class MoeFms {

  static Retrofit newMoeFmRetrofit() {
    new Retrofit.Builder()
        .baseUrl("http://moe.fm/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  static Retrofit mewMoeFouRetrofit() {
    new Retrofit.Builder()
        .baseUrl("https://api.moefou.org/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  static MoeFmRadioService newMoeFmRadioService(
      Retrofit moeFmRetrofit, Retrofit moeFouRetrofit, String apiKey) {
    new MoeFmRadioService(
        moeFmRetrofit.create(MoeFmService), moeFouRetrofit.create(MoeFouService), apiKey)
  }

  static MoeFmRadioService newMoeFmRadioService(String apiKey) {
    newMoeFmRadioService(newMoeFmRetrofit(), mewMoeFouRetrofit(), apiKey)
  }

}
