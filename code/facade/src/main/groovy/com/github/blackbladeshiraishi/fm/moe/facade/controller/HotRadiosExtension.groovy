package com.github.blackbladeshiraishi.fm.moe.facade.controller

import com.github.blackbladeshiraishi.fm.moe.business.business.ListHotRadios
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import groovy.transform.PackageScope
import rx.Observable
import rx.Subscriber

class HotRadiosExtension {

  private final ListHotRadios listHotRadios

  private List<Radio> hotRadios

  @PackageScope
  HotRadiosExtension(ListHotRadios listHotRadios) {
    this.listHotRadios = listHotRadios
  }

  /**
   * suggest {@link Observable#subscribeOn(rx.Scheduler) .subscribe(Schedulers.io())}
   */
  Observable<List<Radio>> hotRadios() {
    return Observable.create({Subscriber<List<Radio>> subscriber ->
      try {
        def result = getHotRadios()
        if (!subscriber.unsubscribed) {
          subscriber.onNext(result)
          subscriber.onCompleted()
        }
      } catch (Throwable e) {
        subscriber.onError(e)
      }
    } as Observable.OnSubscribe<List<Radio>>)
  }

  synchronized private List<Radio> getHotRadios() {
    if (hotRadios != null) {
      return hotRadios
    } else {
      return hotRadios = listHotRadios.execute()
    }
  }

}
