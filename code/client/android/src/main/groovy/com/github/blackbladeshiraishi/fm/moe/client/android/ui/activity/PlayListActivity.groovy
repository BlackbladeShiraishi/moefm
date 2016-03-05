package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.PlayListAdapter
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.MediaPlayControllerViewHolder
import com.github.blackbladeshiraishi.fm.moe.facade.presenter.MediaPlayControllerPresenter

public class PlayListActivity extends AppCompatActivity {

  private RecyclerView recyclerView

  MediaPlayControllerViewHolder mediaPlayControllerViewHolder
  MediaPlayControllerPresenter mediaPlayControllerPresenter

  private PlayListAdapter adapter

  static Intent buildIntent(Context context) {
    new Intent(context, PlayListActivity)
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_play_list)

    recyclerView = findViewById(R.id.play_songs_list) as RecyclerView
    recyclerView.layoutManager = new LinearLayoutManager(this)

    mediaPlayControllerViewHolder =
        new MediaPlayControllerViewHolder(findViewById(R.id.media_controller))
    mediaPlayControllerPresenter = new MediaPlayControllerPresenter(mediaPlayControllerViewHolder)
  }

  @Override
  protected void onStart() {
    super.onStart()

    def playSongComponent = MoeFmApplication.get(this).playSongComponent
    if (playSongComponent == null) {
      Toast.makeText(this, "not playing", Toast.LENGTH_LONG).show()//TODO improve this
    }
    adapter = new PlayListAdapter(playSongComponent.playList, playSongComponent.playService)
    recyclerView.adapter = adapter
    mediaPlayControllerPresenter.bindPlayService(playSongComponent.playService)
  }

  @Override
  protected void onStop() {
    mediaPlayControllerPresenter.unbindPlayService()
    recyclerView.adapter = null
    adapter.release() //!important
    adapter = null
    super.onStop()
  }

  @Override
  protected void onDestroy() {
    mediaPlayControllerPresenter = null
    mediaPlayControllerViewHolder = null
    recyclerView = null
    super.onDestroy()
  }

}
