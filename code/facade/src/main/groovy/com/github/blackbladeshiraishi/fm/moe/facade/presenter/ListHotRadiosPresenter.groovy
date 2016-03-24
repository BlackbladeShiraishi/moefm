package com.github.blackbladeshiraishi.fm.moe.facade.presenter

import com.github.blackbladeshiraishi.fm.moe.business.business.RadioService
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import com.github.blackbladeshiraishi.fm.moe.facade.view.ListHotRadiosView
import com.github.blackbladeshiraishi.fm.moe.facade.view.UiThread
import gq.baijie.rxlist.ObservableList
import rx.Observable
import rx.Observer
import rx.Scheduler
import rx.Subscription
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subjects.Subject
import rx.subscriptions.Subscriptions

import javax.annotation.Nonnull
import javax.annotation.Nullable

@UiThread
class ListHotRadiosPresenter {

  @Nonnull
  final Subject<Event, Event> eventBus = PublishSubject.create()

  @Nonnull
  final Scheduler uiScheduler
  @Nonnull
  final ListHotRadiosView hotRadiosView
  @Nonnull
  final RadioService radioService

  @UiThread
  ObservableList<Radio> hotRadios

  @Nullable
  private Subscription viewSubscription

  ListHotRadiosPresenter(
      @Nonnull ListHotRadiosView hotRadiosView,
      @Nonnull RadioService radioService,
      @Nonnull final Scheduler uiScheduler) {
    this.uiScheduler = uiScheduler
    this.hotRadiosView = hotRadiosView
    this.radioService = radioService
    init()
  }

  @Override
  protected void finalize() throws Throwable {
    cleanup()
    super.finalize()
  }

  private void init() {
    bindView()
  }

  private void cleanup() {
    unbindView()
  }

  private void bindView() {
    unbindView()
    viewSubscription = Subscriptions.from(
        eventBus().ofType(StartLoadEvent).observeOn(uiScheduler).subscribe {
          hotRadiosView.showProgressView()
        },
        eventBus().ofType(LoadFinishEvent).observeOn(uiScheduler).subscribe {
          hotRadiosView.closeProgressView()
        },
        eventBus().ofType(HotRadioListChangeEvent).observeOn(uiScheduler).subscribe {
          HotRadioListChangeEvent hotRadios ->
            hotRadiosView.unbindHotRadios()
            hotRadiosView.bindHotRadios(hotRadios.newList)
        }
    )
  }

  private void unbindView() {
    hotRadiosView.unbindHotRadios()
    if (viewSubscription == null) {
      return
    }
    if (!viewSubscription.unsubscribed) {
      viewSubscription.unsubscribe()
    }
    viewSubscription = null
  }

  public Observable<Event> eventBus() {
    return eventBus.onBackpressureDrop()
  }


  private void createHotRadioList() {
    final ObservableList<Radio> newList = ObservableList.create(new ArrayList<Radio>())
    final ObservableList<Radio> oldList = hotRadios
    hotRadios = newList
    eventBus.onNext(new HotRadioListChangeEvent(this, oldList, newList))
  }

  private void refresh() {

  }

  void start() {
    load()
  }

  private void load() {
    eventBus.onNext(new StartLoadEvent(this))
    createHotRadioList()
    radioService.hotRadios().subscribeOn(Schedulers.io()).observeOn(uiScheduler).subscribe(
        new Observer<Radio>() {

          @Override
          void onCompleted() {
            eventBus.onNext(new LoadFinishEvent(ListHotRadiosPresenter.this))
          }

          @Override
          void onError(Throwable e) {
            eventBus.onNext(new LoadFinishEvent(ListHotRadiosPresenter.this))
            eventBus.onNext(new LoadErrorEvent(ListHotRadiosPresenter.this, e))
          }

          @Override
          void onNext(Radio radio) {
            addRadio(radio)
          }

        })
  }

  private void addRadio(Radio radio) {
    hotRadios.add(radio)
  }

  private static enum State {

    IDLE,
    LOADING,
    LOADED
  }

  static class Event {

    final ListHotRadiosPresenter presenter

    Event(ListHotRadiosPresenter presenter) {
      this.presenter = presenter
    }
  }

  static class HotRadioListChangeEvent extends Event {

    @Nullable
    final ObservableList<Radio> oldList
    @Nullable
    final ObservableList<Radio> newList

    HotRadioListChangeEvent(
        ListHotRadiosPresenter presenter,
        ObservableList<Radio> oldList,
        ObservableList<Radio> newList) {
      super(presenter)
      this.oldList = oldList
      this.newList = newList
    }
  }

  static class StartLoadEvent extends Event {

    StartLoadEvent(ListHotRadiosPresenter presenter) {
      super(presenter)
    }
  }

  static class LoadFinishEvent extends Event {

    LoadFinishEvent(ListHotRadiosPresenter presenter) {
      super(presenter)
    }
  }

  static class LoadErrorEvent extends Event {

    final Throwable error

    LoadErrorEvent(ListHotRadiosPresenter presenter, Throwable error) {
      super(presenter)
      this.error = error
    }
  }

}
