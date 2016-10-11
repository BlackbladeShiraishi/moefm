package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.SongsAdapter;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AlbumView extends FrameLayout {

  private final RecyclerView albumSongListView;

  private final SongsAdapter songsAdapter;

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
    inflater.inflate(R.layout.view_radio_content, this);
    albumSongListView = (RecyclerView) findViewById(R.id.radio_song_list);

    songsAdapter = new SongsAdapter();
    albumSongListView.setAdapter(songsAdapter);
  }

  public void setAlbum(Album album) {
    this.album = album;
  }

  public void refresh() {
    if (album == null) {
      //TODO show message
      return;
    }
    MoeFmApplication.get(getContext()).getAppComponent().getRadioService().albumSongs(album)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Song>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            String message = String.format("[%s]%s", e.getClass().getSimpleName(), e.getMessage());
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
          }

          @Override
          public void onNext(Song song) {
            songsAdapter.getSongs().add(song);
            songsAdapter.notifyItemInserted(songsAdapter.getSongs().size() - 1);
          }
        });
  }

}
