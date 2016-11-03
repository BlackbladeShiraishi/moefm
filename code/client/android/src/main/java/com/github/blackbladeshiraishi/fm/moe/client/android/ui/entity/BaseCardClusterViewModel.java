package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;

import android.view.View;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.CardClusterView.CardClusterViewModel;

import javax.annotation.Nullable;

public abstract class BaseCardClusterViewModel implements CardClusterViewModel {

  public final View.OnClickListener onClickTitleContainerListener;

  public final String title;

  protected BaseCardClusterViewModel(
      @Nullable View.OnClickListener onClickTitleContainerListener,
      @Nullable String title) {
    this.onClickTitleContainerListener = onClickTitleContainerListener;
    this.title = title;
  }

  @Nullable
  @Override
  public View.OnClickListener getOnClickTitleContainerListener() {
    return onClickTitleContainerListener;
  }

  @Nullable
  @Override
  public String getTitle() {
    return title;
  }

}
