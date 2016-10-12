package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SearchView extends FrameLayout {

  public static final String NAME = SearchView.class.getName();

  private final EditText keywordInputView;

  private final ContentListView contentListView;

  public SearchView(Context context) {
    super(context);
  }

  public SearchView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_search, this);
    keywordInputView = (EditText) findViewById(R.id.keyword_input);
    contentListView = (ContentListView) findViewById(R.id.content_list);

    subscribe();
  }

  private void subscribe() {
    RxTextView.textChanges(keywordInputView) //!NOTE: map MUST be first, see javadoc of textChanges
        .map(new Func1<CharSequence, String>() {
          @Override
          public String call(CharSequence charSequence) {
            return String.valueOf(charSequence).trim();
          }
        })
        .debounce(500, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .switchMap(new Func1<String, Observable<? extends List<Content>>>() {
          @Override
          public Observable<? extends List<Content>> call(String s) {
            if (!s.isEmpty()) {
              return MoeFmApplication.get(getContext()).getAppComponent().getRadioService()
                  .searchContents(s, "radio,music")
                  .onErrorResumeNext(Observable.<List<Content>>empty());
            } else {
              return Observable.just(Collections.<Content>emptyList());
            }
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<Content>>() {
          @Override
          public void call(List<Content> contents) {
            contentListView.setContent(contents);
          }
        });
  }

}
