package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

import com.github.blackbladeshiraishi.fm.moe.business.business.ListHotRadios
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
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

  static ListHotRadios newListHotRadios(Retrofit retrofit, String apiKey) {
    new MoeFmListHotRadios(retrofit: retrofit, apiKey: apiKey)
  }

  static List<Radio> listHotRadios(Retrofit retrofit, String apiKey) {
    newListHotRadios(retrofit, apiKey).execute()
  }

}
