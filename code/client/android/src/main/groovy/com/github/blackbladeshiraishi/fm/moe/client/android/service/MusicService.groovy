package com.github.blackbladeshiraishi.fm.moe.client.android.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song

class MusicService extends Service {

  public static final String EXTRA_SONG = "extra_song"

  static Intent buildPlaySongIntent(Context context, Song song) {
    Intent intent = new Intent(context, MusicService)
    intent.action = Intent.ACTION_VIEW
    intent.putExtra(EXTRA_SONG, song)
    return intent
  }
  static void playSong(Context context, Song song) {
    context.startService(buildPlaySongIntent(context, song))
  }

  public MusicService() {
  }

  @Override
  void onCreate() {
    super.onCreate()
    MoeFmApplication.get(this).createPlaySongComponent()
  }

  @Override
  void onDestroy() {
    MoeFmApplication.get(this).releasePlaySongComponent()
    super.onDestroy()
  }

  @Override
  int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId)
    if (intent != null) {
      if (intent.action == Intent.ACTION_VIEW) {
        Song song = intent.getSerializableExtra(EXTRA_SONG) as Song
        MoeFmApplication.get(this).playSongComponent.with {
          playList.add(song)
          if (playService.location == playList.size()) {
            playService.location = playList.size() - 1
          }
          playService.play()
        }
      }
    } else {
      //TODO
    }
    return START_STICKY //TODO selfStop
  }

  @Override
  IBinder onBind(Intent intent) {
    return null
  }

}
