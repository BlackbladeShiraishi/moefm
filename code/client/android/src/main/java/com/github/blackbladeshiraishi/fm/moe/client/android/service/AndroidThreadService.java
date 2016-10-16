package com.github.blackbladeshiraishi.fm.moe.client.android.service;

import com.github.blackbladeshiraishi.fm.moe.business.api.ThreadService;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Singleton
public class AndroidThreadService implements ThreadService {

  @Inject
  public AndroidThreadService() {
  }

  @Override
  public Scheduler getSingleThreadScheduler() {
    return AndroidSchedulers.mainThread();
  }

}
