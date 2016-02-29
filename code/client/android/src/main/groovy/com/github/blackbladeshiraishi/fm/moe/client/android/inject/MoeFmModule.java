package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.business.RadioService;
import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.MoeFms;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.qualifier.MoeFm;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.qualifier.MoeFou;

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
  @MoeFm
  @Singleton
  RadioService privideMoeFmRadioService(@MoeFm Retrofit retrofit) {
    return MoeFms.newMoeFmRadioService(retrofit, apiKey);
  }

  @Provides
  @MoeFou
  @Singleton
  RadioService privideMoeFouRadioService(@MoeFou Retrofit retrofit) {
    return MoeFms.newMoeFmRadioService(retrofit, apiKey);
  }

}
