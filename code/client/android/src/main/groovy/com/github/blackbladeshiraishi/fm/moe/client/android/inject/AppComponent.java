package com.github.blackbladeshiraishi.fm.moe.client.android.inject;

import com.github.blackbladeshiraishi.fm.moe.business.business.ListHotRadios;

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
