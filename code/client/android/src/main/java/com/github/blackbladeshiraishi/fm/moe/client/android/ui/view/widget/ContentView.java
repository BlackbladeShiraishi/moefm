package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.TabAdapter;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentView extends FrameLayout {

  private final List<TabAdapter.Tab> tabs;
  private final TabAdapter tabAdapter;

  private final SongListView songListView;
  private ContentIntroductionView contentIntroductionView;

  private Content content;

  public ContentView(Context context) {
    super(context);
  }

  public ContentView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ContentView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_content, this);
    ViewPager tabsView = (ViewPager) findViewById(R.id.tabs_view);

    // Tab Views
    tabs = new ArrayList<>(2);
    // song list
    songListView = new SongListView(getContext());
    tabs.add(new TabAdapter.Tab("曲目", songListView));

    tabAdapter = new TabAdapter(tabs);
    tabsView.setAdapter(tabAdapter);
  }

  public void setContent(Content content) {
    ContentIntroductionView contentIntroductionView;
    if ("radio".equals(content.getType())) {
      contentIntroductionView = new RadioIntroductionView(getContext());
    } else if ("music".equals(content.getType())) {
      contentIntroductionView = new AlbumIntroductionView(getContext());
    } else {
      String message = "unknown content type: " + content.getType();
      Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
      return;
    }
    this.content = content;
    contentIntroductionView.setContent(content);
    setContentIntroductionView(contentIntroductionView);
  }

  private void setContentIntroductionView(@Nonnull ContentIntroductionView introductionView) {
    this.contentIntroductionView = introductionView;
    if (tabs.size() == 1) {
      tabs.add(0, new TabAdapter.Tab("简介", introductionView));
    } else if (tabs.size() == 2) {
      tabs.set(0, new TabAdapter.Tab("简介", introductionView));
    } else {
      throw new IllegalStateException("illegal tabs.size():" + tabs.size());
    }
    tabAdapter.notifyDataSetChanged();
  }

  public void refresh() {
    if (content == null) {
      //TODO show message
      return;
    }
    contentIntroductionView.setContent(content);
    MoeFmApplication.get(getContext()).getAppComponent().getRadioService().radioSongs(content.getId())
        .subscribeOn(Schedulers.io())
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Song>>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            String message = String.format("[%s]%s", e.getClass().getSimpleName(), e.getMessage());
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
          }

          @Override
          public void onNext(List<Song> songList) {
            songListView.setSongList(songList);
          }
        });
  }

}
