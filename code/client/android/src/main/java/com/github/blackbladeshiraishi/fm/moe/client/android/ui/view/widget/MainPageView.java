package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPageView extends FrameLayout {

  public static final String NAME = MainPageView.class.getName();

  private final android.support.v7.widget.SearchView searchView;
  private final FrameLayout contentContainerView;

  private boolean isShowingSearchResult = false;

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
    LayoutInflater.from(getContext()).inflate(R.layout.view_main_page, this);
    searchView = (android.support.v7.widget.SearchView) findViewById(R.id.search);
    contentContainerView = (FrameLayout) findViewById(R.id.content_container);

    if (searchView != null) {
      RxSearchView.queryTextChangeEvents(searchView)
          .filter(SearchViewQueryTextEvent::isSubmitted)
          .map(event->event.queryText().toString())
          .observeOn(Schedulers.io())
          .switchMap(s->{
            if (!s.isEmpty()) {
              return MoeFmApplication.get(getContext()).getAppComponent().getRadioService()
                  .searchContents(s, "radio,music")
                  .onErrorResumeNext(Observable.empty());
            } else {
              return Observable.just(Collections.<Content>emptyList());
            }
          })
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(result->{
            cancelLoadMainPage();
            showSearchResult(result);
          });
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    loadMainPage();
  }

  @Override
  protected void onDetachedFromWindow() {
    cancelLoadMainPage();
    super.onDetachedFromWindow();
  }

  private void loadMainPage() {
    cancelLoadMainPage();
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

  private void cancelLoadMainPage() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
  }

  private void showLoading() {
    View v = LayoutInflater.from(getContext()).inflate(R.layout.view_loading_progress, this, false);
    setContentView(v);
  }

  private void showMainPage(MoeFmMainPage moeFmMainPage) {
    NestedScrollView scrollView = new NestedScrollView(getContext());
    MainPageContentView contentView = new MainPageContentView(getContext());
    contentView.setMainPage(moeFmMainPage, 4);
    scrollView.addView(contentView);
    setContentView(scrollView);
  }

  private void showSearchResult(List<Content> contents) {
    final ContentListView contentListView = new ContentListView(getContext());
    contentListView.setContent(contents);
    setContentView(contentListView);
    // update state
    isShowingSearchResult = true;
  }

  private void setContentView(View contentView) {
    contentContainerView.removeAllViews();
    contentContainerView.addView(contentView);
  }

  // ########## Input:onBackPressed ##########
  {
    // set Focusable to receive KeyEvent
    setFocusable(true);
    setFocusableInTouchMode(true);
  }

  //TODO recheck this method
  //reference: http://android-developers.blogspot.in/2009/12/back-and-other-hard-keys-three-stories.html
  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
      return super.dispatchKeyEvent(event);
    }

    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0
        ) {

      // Tell the framework to start tracking this event.
      getKeyDispatcherState().startTracking(event, this);
      return true;

    } else if (event.getAction() == KeyEvent.ACTION_UP) {
      getKeyDispatcherState().handleUpEvent(event);
      if (event.isTracking() && !event.isCanceled()) {

        // DO BACK ACTION HERE
        if (onBackPressed()) {
          return true;
        }

      }
    }
    return super.dispatchKeyEvent(event);
  }

  private boolean onBackPressed() {
    return exitSearchResultView();
  }

  private boolean exitSearchResultView() {
    if (isShowingSearchResult) {
      isShowingSearchResult = false;
      loadMainPage();
      return true;
    } else {
      return false;
    }
  }
  // ########## Input:onBackPressed End ##########

}
