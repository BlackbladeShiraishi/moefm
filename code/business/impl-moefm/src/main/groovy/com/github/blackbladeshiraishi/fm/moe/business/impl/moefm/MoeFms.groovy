package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import retrofit2.Retrofit

class MoeFms {

  static Retrofit newRetrofit() {
    new Retrofit.Builder()
        .baseUrl("http://moe.fm")
        .build();
  }

  static List<Radio> listHotRadios(Retrofit retrofit, String apiKey) {
    new MoeFmListHotRadios(retrofit: retrofit, apiKey: apiKey).execute()
  }

}
