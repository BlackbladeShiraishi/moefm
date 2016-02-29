package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.MoeFms;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.qualifier.MoeFm;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.qualifier.MoeFou;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class RetrofitModule {

  @Provides
  @MoeFm
  @Singleton
  Retrofit provoideMoeFmRetrofit() {
    return MoeFms.newMoeFmRetrofit();
  }


  @Provides
  @MoeFou
  @Singleton
  Retrofit provoideMoeFouRetrofit() {
    return MoeFms.mewMoeFouRetrofit();
  }

}
