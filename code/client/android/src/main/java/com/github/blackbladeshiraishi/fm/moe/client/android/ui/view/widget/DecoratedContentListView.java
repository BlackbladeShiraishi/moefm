package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.List;
import java.util.Locale;

import flow.Flow;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class DecoratedContentListView extends FrameLayout {

  public static final String RADIO_LIST =
      "com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.DecoratedContentListView.RADIO_LIST";
  public static final String ALBUM_LIST =
      "com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.DecoratedContentListView.ALBUM_LIST";

  private Subscription subscription;

  public DecoratedContentListView(Context context) {
    super(context);
  }

  public DecoratedContentListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DecoratedContentListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DecoratedContentListView(Context context, AttributeSet attrs, int defStyleAttr,
                                  int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
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

    final Context context = getContext();
    String key = Flow.getKey(context);
    if (key == null) {
      key = "";
    }
    final Observable<List<Content>> contents;
    switch (key) {
      case RADIO_LIST:
        contents = MoeFmApplication.get(context).getAppComponent().getSessionService().radios();
        break;
      case ALBUM_LIST:
        contents = MoeFmApplication.get(context).getAppComponent().getSessionService().albums();
        break;
      default:
        contents = Observable.empty();
        Toast.makeText(context, "unknown key: " + key, Toast.LENGTH_LONG).show();
        break;
    }
    subscription = contents
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Content>>() {
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
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
          }

          @Override
          public void onNext(List<Content> contentList) {
            showContentList(contentList);
          }
        });
  }

  private void unsubscribe() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
  }

  private void showContentList(List<Content> contentList) {
    ContentListView contentListView = new ContentListView(getContext());
    contentListView.setContent(contentList);
    setContentView(contentListView);
  }

  private void showLoading() {
    View v = LayoutInflater.from(getContext()).inflate(R.layout.view_loading_progress, this, false);
    setContentView(v);
  }

  private void setContentView(View contentView) {
    removeAllViews();
    addView(contentView);
  }
}
