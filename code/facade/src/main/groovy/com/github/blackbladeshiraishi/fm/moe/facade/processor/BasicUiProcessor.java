package com.github.blackbladeshiraishi.fm.moe.facade.processor;

import com.github.blackbladeshiraishi.fm.moe.business.io.event.EventBus;
import com.github.blackbladeshiraishi.fm.moe.facade.controller.Controller;
import com.github.blackbladeshiraishi.fm.moe.facade.view.UiThread;
import com.github.blackbladeshiraishi.fm.moe.facade.view.event.ShowRadioEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Subscription;
import rx.functions.Action1;

@UiThread
public class BasicUiProcessor {

  @Nonnull
  final EventBus eventBus;

  @Nonnull
  final Controller controller;

  @Nullable
  Subscription subscription;


  public BasicUiProcessor(
      @UiThread @Nonnull EventBus eventBus,
      @UiThread @Nonnull Controller controller) {
    this.eventBus = eventBus;
    this.controller = controller;
  }

  @Override
  protected void finalize() throws Throwable {
    //TODO log leak
    unregister();
    super.finalize();
  }

  public void register() {
    if (subscription != null && !subscription.isUnsubscribed()) {
      return;
    }
    subscription = eventBus.eventStream().ofType(ShowRadioEvent.class)
        .subscribe(new Action1<ShowRadioEvent>() {
          @Override
          public void call(ShowRadioEvent showRadioEvent) {
            controller.showRadio(showRadioEvent.getRadio(), showRadioEvent);
          }
        });
  }

  private void unregister() {
    if (subscription != null) {
      if (!subscription.isUnsubscribed()) {
        subscription.unsubscribe();
      }
      subscription = null;
    }
  }

}
