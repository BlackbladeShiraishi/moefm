package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.CardClusterViewModelListAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity.MoeFmMainPageAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardClusterViewHolder;

import java.util.List;
import java.util.Locale;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainPageView extends RecyclerView {

  public static final String NAME = MainPageView.class.getName();

  private Subscription subscription;

  public MainPageView(Context context) {
    super(context);
  }

  public MainPageView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MainPageView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  // init
  {
    setLayoutManager(new LinearLayoutManager(getContext()));
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    subscribe();
  }

  @Override
  protected void onDetachedFromWindow() {
    unsubscribe();
    super.onDetachedFromWindow();
  }

  private void subscribe() {
    unsubscribe();
    subscription = MoeFmApplication.get(getContext()).getAppComponent().getSessionService().mainPage()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<MoeFmMainPage>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable error) {
            String message = String.format(
                Locale.US, "[%s]%s", error.getClass().getSimpleName(), error.getMessage());
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
          }

          @Override
          public void onNext(MoeFmMainPage moeFmMainPage) {
            final List<CardClusterViewHolder.CardClusterViewModel> dataSet =
                MoeFmMainPageAdapter.newCardClusterViewModelList(moeFmMainPage);
            setAdapter(new CardClusterViewModelListAdapter(dataSet, 4));
          }
        });
  }

  private void unsubscribe() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
  }

}
