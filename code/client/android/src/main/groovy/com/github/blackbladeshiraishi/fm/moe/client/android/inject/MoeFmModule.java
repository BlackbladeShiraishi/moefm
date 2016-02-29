package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.business.RadioService;
import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.MoeFms;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MoeFmModule {

  final String apiKey;

  public MoeFmModule(String apiKey) {
    this.apiKey = apiKey;
  }

  @Provides
  @Singleton
  RadioService privideRadioService(Retrofit retrofit) {
    return MoeFms.newMoeFmRadioService(retrofit, apiKey);
  }

}
