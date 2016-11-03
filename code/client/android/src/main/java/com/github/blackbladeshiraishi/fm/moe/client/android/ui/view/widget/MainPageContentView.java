package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity.MoeFmMainPageAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.CardClusterView.CardClusterViewModel;

import java.util.List;

public class MainPageContentView extends LinearLayout {

  public MainPageContentView(Context context) {
    super(context);
  }

  public MainPageContentView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MainPageContentView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public MainPageContentView(
      Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    setOrientation(VERTICAL);
  }

  public void setMainPage(MoeFmMainPage moeFmMainPage, int maxColumn) {
    // adapt data structure
    final List<CardClusterViewModel> dataSet =
        MoeFmMainPageAdapter.newCardClusterViewModelList(moeFmMainPage);
    // update child views
    removeAllViews();
    for(CardClusterViewModel model : dataSet) {
      CardClusterView cardClusterView = new CardClusterView(getContext());
      cardClusterView.bindData(model, maxColumn);
      addView(cardClusterView);
    }
  }
}
