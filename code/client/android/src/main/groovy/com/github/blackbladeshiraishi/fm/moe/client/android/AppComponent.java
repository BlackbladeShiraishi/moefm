package com.github.blackbladeshiraishi.fm.moe.client.android;

import com.github.blackbladeshiraishi.fm.moe.business.business.ListHotRadios;
import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.inject.MoeFmModule;
import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.inject.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    RetrofitModule.class,
    MoeFmModule.class
})
public interface AppComponent {
  ListHotRadios getListHotRadios();
}
