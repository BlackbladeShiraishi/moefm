package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.TabAdapter;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentView extends FrameLayout {

  private final List<TabAdapter.Tab> tabs;
  private final TabAdapter tabAdapter;

  private View songListView;
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
    tabAdapter = new TabAdapter(tabs);
    tabsView.setAdapter(tabAdapter);

    final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    tabLayout.setupWithViewPager(tabsView, true);
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

  private void setContentIntroductionView(@Nullable ContentIntroductionView introductionView) {
    this.contentIntroductionView = introductionView;
    notifyDataSetChanged();
  }

  private void setSongListView(@Nullable View songListView) {
    this.songListView = songListView;
    notifyDataSetChanged();
  }

  private void notifyDataSetChanged() {
    tabs.clear();
    if (contentIntroductionView != null) {
      tabs.add(new TabAdapter.Tab("简介", contentIntroductionView));
    }
    if (songListView != null) {
      tabs.add(new TabAdapter.Tab("曲目", songListView));
    }
    tabAdapter.notifyDataSetChanged();
  }

  public void refresh() {
    if (content == null) {
      //TODO show message
      return;
    }
    contentIntroductionView.setContent(content);
    refreshSongList();
  }

  private void refreshSongList() {
    Observable<Song> songObservable;
    if ("radio".equals(content.getType())) {
      songObservable = MoeFmApplication.get(getContext()).getAppComponent().getRadioService()
          .radioSongs(content.getId());
    } else if ("music".equals(content.getType())) {
      songObservable = MoeFmApplication.get(getContext()).getAppComponent().getRadioService()
          .albumSongs(content.getId());
    } else {
      songObservable = Observable.empty();
    }
    songObservable
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
            showLoadedSongList(songList);
          }
        });
  }

  private void showLoadedSongList(List<Song> loadedSongList) {
    SongListView songListView = new SongListView(getContext());
    songListView.setSongList(loadedSongList);
    setSongListView(songListView);
  }

}
