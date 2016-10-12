package com.github.blackbladeshiraishi.fm.moe.business.api;

import rx.Scheduler;

public interface ThreadService {

  Scheduler getSingleThreadScheduler();

}
