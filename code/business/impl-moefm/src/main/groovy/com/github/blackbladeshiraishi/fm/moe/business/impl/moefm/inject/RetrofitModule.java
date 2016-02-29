package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.inject;

import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.MoeFms;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class RetrofitModule {

  @Provides
  @Singleton
  Retrofit provoideRetrofit() {
    return MoeFms.newRetrofit();
  }

}
