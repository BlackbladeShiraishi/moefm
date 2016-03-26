package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.HotRadiosAdapter
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import com.github.blackbladeshiraishi.fm.moe.facade.view.BaseView
import com.github.blackbladeshiraishi.fm.moe.facade.view.ListHotRadiosView
import gq.baijie.rxlist.ObservableList

import javax.annotation.Nonnull

class ListHotRadiosViewHolder extends BaseView<ListHotRadiosView> implements ListHotRadiosView {

  final View rootView
  final View contentProgress
  final RecyclerView hotRadioList
  final HotRadiosAdapter hotRadiosAdapter

  ListHotRadiosViewHolder(@Nonnull View rootView) {
    this.rootView = rootView
    contentProgress = rootView.findViewById(R.id.content_progress)
    hotRadioList = rootView.findViewById(R.id.hot_radios_list) as RecyclerView
    hotRadiosAdapter = new HotRadiosAdapter()
    init()
  }

  private void init() {
    hotRadioList.layoutManager = new LinearLayoutManager(hotRadioList.context)
    hotRadioList.adapter = hotRadiosAdapter
  }

  @Override
  protected void finalize() throws Throwable {
    unbindHotRadios()
    super.finalize()
  }

  @Override
  void showProgressView() {
    contentProgress.visibility = View.VISIBLE
  }

  @Override
  void closeProgressView() {
    contentProgress.visibility = View.GONE
  }

  @Override
  void bindHotRadios(ObservableList<Radio> hotRadios) {
    hotRadiosAdapter.bindRadios(hotRadios)
  }

  @Override
  void unbindHotRadios() {
    hotRadiosAdapter.unbindRadios()
  }

}
