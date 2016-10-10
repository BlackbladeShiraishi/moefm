package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.AndroidListHotRadiosView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.MainLayoutView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.MainPageView;
import com.github.blackbladeshiraishi.fm.moe.facade.presenter.ListHotRadiosPresenter;

import java.util.Map;

import flow.Direction;
import flow.Flow;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.State;
import flow.TraversalCallback;
import rx.android.schedulers.AndroidSchedulers;


public class Main0Activity extends AppCompatActivity {

  private MainLayoutView layoutView;

  // ##### flow #####
  @Override
  protected void attachBaseContext(Context newBase) {
    newBase = Flow.configure(newBase, this)
        .dispatcher(KeyDispatcher.configure(this, new MainKeyChanger()).build())
        .defaultKey(MainPageView.NAME)
        .install();
    super.attachBaseContext(newBase);
  }

  @Override
  public void onBackPressed() {
    if (!Flow.get(this).goBack()) {
      super.onBackPressed();
    }
  }
  // ##### flow end #####


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    layoutView = new MainLayoutView(this);
    setContentView(layoutView);
  }

  private class MainKeyChanger implements KeyChanger {
    @Override
    public void changeKey(@Nullable State outgoingState, @NonNull State incomingState,
                          @NonNull Direction direction,
                          @NonNull Map<Object, Context> incomingContexts,
                          @NonNull TraversalCallback callback) {
      final Object incomingKey = incomingState.getKey();
      final Context incomingContext = incomingContexts.get(incomingKey);
      if (incomingKey.equals(MainPageView.NAME)) {
        MainPageView contentView = new MainPageView(incomingContext);
        layoutView.setContentView(contentView);
        contentView.refresh();
      } else if (incomingKey.equals(AndroidListHotRadiosView.NAME)) {
        AndroidListHotRadiosView contentView = new AndroidListHotRadiosView(incomingContext);
        layoutView.setContentView(contentView);
        ListHotRadiosPresenter presenter = new ListHotRadiosPresenter(
            MoeFmApplication.get(incomingContext).getAppComponent().getRadioService(),
            AndroidSchedulers.mainThread()
        );
        presenter.start();
        presenter.bindView(contentView);
        contentView.setTag(presenter);
      } //TODO else
      callback.onTraversalCompleted();
    }
  }

}
