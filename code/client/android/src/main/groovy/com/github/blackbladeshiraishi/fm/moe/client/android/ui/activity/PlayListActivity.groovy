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

public class PlayListActivity extends AppCompatActivity {

  private RecyclerView recyclerView

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
  }

  @Override
  protected void onStart() {
    super.onStart()

    MoeFmApplication application = MoeFmApplication.get(this)
    if (application.playSongComponent == null) {
      Toast.makeText(this, "not playing", Toast.LENGTH_LONG).show()//TODO improve this
    }
    adapter = new PlayListAdapter(application.playSongComponent.playList)
    recyclerView.adapter = adapter
  }

  @Override
  protected void onStop() {
    recyclerView.adapter = null
    adapter.release() //!important
    adapter = null
    super.onStop()
  }

}
