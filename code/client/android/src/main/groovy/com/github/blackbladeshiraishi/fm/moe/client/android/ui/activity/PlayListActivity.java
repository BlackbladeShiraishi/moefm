package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.PlaySongComponent;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.PlayListAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.MediaPlayControllerViewHolder;
import com.github.blackbladeshiraishi.fm.moe.facade.presenter.MediaPlayControllerPresenter;
import rx.android.schedulers.AndroidSchedulers;

public class PlayListActivity extends AppCompatActivity {

  private RecyclerView recyclerView;

  MediaPlayControllerViewHolder mediaPlayControllerViewHolder;
  MediaPlayControllerPresenter mediaPlayControllerPresenter;

  private PlayListAdapter adapter;

  public static Intent buildIntent(Context context) {
    return new Intent(context, PlayListActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play_list);

    recyclerView = (RecyclerView) findViewById(R.id.play_songs_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    mediaPlayControllerViewHolder =
        new MediaPlayControllerViewHolder(findViewById(R.id.media_controller));
    mediaPlayControllerPresenter = new MediaPlayControllerPresenter(
        mediaPlayControllerViewHolder, AndroidSchedulers.mainThread());
  }

  @Override
  protected void onStart() {
    super.onStart();

    PlaySongComponent playSongComponent = MoeFmApplication.get(this).getPlaySongComponent();
    if (playSongComponent == null) {
      Toast.makeText(this, "not playing", Toast.LENGTH_LONG).show();//TODO improve this
      finish();
      return;
    }
    adapter = new PlayListAdapter(playSongComponent.getPlayList(), playSongComponent.getPlayService());
    recyclerView.setAdapter(adapter);
    mediaPlayControllerPresenter.bindPlayService(playSongComponent.getPlayService());
  }

  @Override
  protected void onStop() {
    if (mediaPlayControllerPresenter != null) {
      mediaPlayControllerPresenter.unbindPlayService();
    }
    recyclerView.setAdapter(null);
    if (adapter != null) {
      adapter.release(); //!important
      adapter = null;
    }
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    mediaPlayControllerPresenter = null;
    mediaPlayControllerViewHolder = null;
    recyclerView = null;
    super.onDestroy();
  }

}
