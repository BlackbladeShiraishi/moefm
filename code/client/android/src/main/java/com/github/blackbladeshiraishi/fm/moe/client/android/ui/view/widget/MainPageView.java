package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;

import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPageView extends FrameLayout {

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
    subscription = Observable
        .fromCallable(() -> MoeFmApplication.get(getContext()).getAppComponent().getSessionService().mainPage())
        .flatMap(it -> it)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<MoeFmMainPage>() {
          @Override
          public void onStart() {
            super.onStart();
            showLoading();
          }

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
            showMainPage(moeFmMainPage);
          }
        });
  }

  private void unsubscribe() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
  }

  private void showLoading() {
    View v = LayoutInflater.from(getContext()).inflate(R.layout.view_loading_progress, this, false);
    setContentView(v);
  }

  private void showMainPage(MoeFmMainPage moeFmMainPage) {
    ScrollView scrollView = new ScrollView(getContext());
    MainPageContentView contentView = new MainPageContentView(getContext());
    contentView.setMainPage(moeFmMainPage, 4);

    scrollView.addView(contentView);
    setContentView(scrollView);
  }

  private void setContentView(View contentView) {
    removeAllViews();
    addView(contentView);
  }

}
