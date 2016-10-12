package com.github.blackbladeshiraishi.fm.moe.business.impl.core.utils;

import rx.Observable;
import rx.Subscriber;

public class SkipCompleteRxOperator<T> implements Observable.Operator<T, T> {
  @Override
  public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
    Subscriber<T> result = new Subscriber<T>() {
      @Override
      public void onCompleted() {
        // skip it
      }

      @Override
      public void onError(Throwable e) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onError(e);
        }
      }

      @Override
      public void onNext(T albumList) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(albumList);
        }
      }
    };
    subscriber.add(result);
    return result;
  }
}
