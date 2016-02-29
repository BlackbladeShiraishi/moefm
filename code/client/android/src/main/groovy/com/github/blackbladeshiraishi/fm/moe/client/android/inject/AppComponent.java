package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.business.RadioService;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.qualifier.MoeFm;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.qualifier.MoeFou;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    RetrofitModule.class,
    MoeFmModule.class
})
public interface AppComponent {

  @MoeFm
  RadioService getMoeFmRadioService();

  @MoeFou
  RadioService getMoeFouRadioService();

}
