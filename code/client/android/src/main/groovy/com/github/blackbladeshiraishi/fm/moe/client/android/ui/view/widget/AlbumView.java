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
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AlbumView extends FrameLayout {

  private final AlbumIntroductionView albumIntroductionView;
  private final SongListView albumSongListView;

  private Album album;

  public AlbumView(Context context) {
    super(context);
  }

  public AlbumView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AlbumView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public AlbumView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_radio, this);
    ViewPager tabsView = (ViewPager) findViewById(R.id.tabs_view);

    // Tab Views
    List<TabAdapter.Tab> tabs = new ArrayList<>(2);
    // introduction view
    albumIntroductionView = new AlbumIntroductionView(getContext());
    tabs.add(new TabAdapter.Tab("简介", albumIntroductionView));
    // song list
    albumSongListView = new SongListView(getContext());
    tabs.add(new TabAdapter.Tab("曲目", albumSongListView));

    tabsView.setAdapter(new TabAdapter(tabs));
  }

  public void setAlbum(Album album) {
    this.album = album;
  }

  public void refresh() {
    if (album == null) {
      //TODO show message
      return;
    }
    albumIntroductionView.setAlbum(album);
    MoeFmApplication.get(getContext()).getAppComponent().getRadioService().albumSongs(album)
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
            albumSongListView.setSongList(songList);
          }
        });
  }

}
