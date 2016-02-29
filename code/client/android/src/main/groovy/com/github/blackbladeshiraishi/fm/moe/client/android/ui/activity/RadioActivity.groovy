package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.SongsAdapter
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class RadioActivity extends AppCompatActivity {

  static String EXTRA_NAME_RADIO = "extra.name.radio"

  final SongsAdapter songsAdapter = new SongsAdapter()

  Radio radio

  static Intent buildIntent(Context context, Radio radio) {
    def intent = new Intent(context, RadioActivity)
    intent.putExtra(EXTRA_NAME_RADIO, radio)
    return intent
  }

  static void startThis(Context context, Radio radio) {
    context.startActivity(buildIntent(context, radio))
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_radio)

    if (intent.hasExtra(EXTRA_NAME_RADIO)) {
      radio = intent.extras.getSerializable(EXTRA_NAME_RADIO) as Radio
    } else {
      throw new IllegalStateException("neet extra: $EXTRA_NAME_RADIO")
    }

    title = radio.title

    (findViewById(R.id.radio_songs_list) as RecyclerView).with {
      adapter = songsAdapter
      layoutManager = new LinearLayoutManager(this)
    }

    MoeFmApplication.get(this).appComponent.moeFouRadioService.radioSongs(radio)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {Song song ->
          songsAdapter.with {
            songs.add(song)
            notifyItemInserted(songs.size() - 1)
          }
        }
  }

}
