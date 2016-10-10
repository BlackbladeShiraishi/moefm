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
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RadioView extends FrameLayout {

  private final RecyclerView radioSongListView;

  private final SongsAdapter songsAdapter;

  private Radio radio;

  public RadioView(Context context) {
    super(context);
  }

  public RadioView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RadioView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public RadioView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_radio_content, this);
    radioSongListView = (RecyclerView) findViewById(R.id.radio_song_list);

    songsAdapter = new SongsAdapter();
    radioSongListView.setAdapter(songsAdapter);
  }

  public void setRadio(Radio radio) {
    this.radio = radio;
  }

  public void refresh() {
    if (radio == null) {
      //TODO show message
      return;
    }
    MoeFmApplication.get(getContext()).getAppComponent().getRadioService().radioSongs(radio)
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
