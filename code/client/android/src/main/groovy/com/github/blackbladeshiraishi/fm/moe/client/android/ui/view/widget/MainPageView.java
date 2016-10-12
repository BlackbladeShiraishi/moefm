package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MainPage;
import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.CardClusterViewModelListAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity.MoeFmMainPageAdapter;

import java.util.Locale;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPageView extends RecyclerView {

  public static final String NAME = MainPageView.class.getName();

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

  public void refresh() {
    MoeFmApplication.get(getContext()).getAppComponent().getRadioService().mainPage()
        .subscribeOn(Schedulers.io())
        .first()
        .toSingle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleSubscriber<MainPage>() {

          @Override
          public void onSuccess(MainPage value) {
            if (value instanceof MoeFmMainPage) {
              setAdapter(new CardClusterViewModelListAdapter(
                  MoeFmMainPageAdapter.newCardClusterViewModelList((MoeFmMainPage) value), 4
              ));
            }
          }

          @Override
          public void onError(Throwable error) {
            String message = String.format(
                Locale.US, "[%s]%s", error.getClass().getSimpleName(), error.getMessage());
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
          }

        });
  }

}
