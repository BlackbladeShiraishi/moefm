package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.HotRadiosAdapter;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.facade.view.EventBusHelper;
import com.github.blackbladeshiraishi.fm.moe.facade.view.ListHotRadiosView;

import javax.annotation.Nonnull;

import gq.baijie.rxlist.ObservableList;
import rx.Observable;

public class AndroidListHotRadiosView extends FrameLayout implements ListHotRadiosView {

  public static final String NAME = AndroidListHotRadiosView.class.getName();

  private final RecyclerView hotRadioList;
  private final View contentProgress;

  private final HotRadiosAdapter hotRadiosAdapter;

  private final EventBusHelper<Event<ListHotRadiosView>> eventBusHelper = EventBusHelper.create();

  public AndroidListHotRadiosView(Context context) {
    super(context);
  }

  public AndroidListHotRadiosView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AndroidListHotRadiosView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public AndroidListHotRadiosView(
      Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_list_hot_radios_content, this);
    inflater.inflate(R.layout.view_list_hot_radios_progress, this);
    hotRadioList = (RecyclerView) findViewById(R.id.hot_radios_list);
    contentProgress = findViewById(R.id.content_progress);

    hotRadiosAdapter = new HotRadiosAdapter();
    hotRadioList.setAdapter(hotRadiosAdapter);
  }

  @Override
  public void showProgressView() {
    contentProgress.setVisibility(VISIBLE);
  }

  @Override
  public void closeProgressView() {
    contentProgress.setVisibility(GONE);
  }

  @Override
  public void bindHotRadios(ObservableList<Radio> hotRadios) {
    hotRadiosAdapter.bindRadios(hotRadios);
  }

  @Override
  public void unbindHotRadios() {
    hotRadiosAdapter.unbindRadios();
  }

  @Nonnull
  @Override
  public Observable<Event<ListHotRadiosView>> eventBus() {
    return eventBusHelper.eventBus();
  }

}
