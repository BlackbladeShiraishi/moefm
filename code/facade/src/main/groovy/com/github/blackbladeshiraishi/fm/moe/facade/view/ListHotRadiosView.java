package com.github.blackbladeshiraishi.fm.moe.facade.view;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import gq.baijie.rxlist.ObservableList;

public interface ListHotRadiosView extends View<ListHotRadiosView, View.Event<ListHotRadiosView>> {

  void showProgressView();

  void closeProgressView();

  void bindHotRadios(ObservableList<Radio> hotRadios);

  void unbindHotRadios();

  class StartEvent extends Event<ListHotRadiosView> {

    public StartEvent(ListHotRadiosView view) {
      super(view);
    }
  }

  class StopEvent extends Event<ListHotRadiosView> {

    public StopEvent(ListHotRadiosView view) {
      super(view);
    }
  }
}
