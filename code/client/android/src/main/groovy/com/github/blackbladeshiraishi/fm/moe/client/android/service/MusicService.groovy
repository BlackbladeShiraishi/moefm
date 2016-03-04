package com.github.blackbladeshiraishi.fm.moe.client.android.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v7.app.NotificationCompat
import com.github.blackbladeshiraishi.fm.moe.business.business.PlayService
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity.PlayListActivity
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song

import javax.annotation.Nullable

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

    setForegroundNotification()
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

  private void setForegroundNotification() {
    bindPlayService()
  }

  private void bindPlayService() {
    MoeFmApplication.get(this).playSongComponent.with {
      playService.eventBus().subscribe {PlayService.Event event ->
        Song song = event.location < playList.size() ? playList.get(event.location) : null
        updateNotification(song, event.state)
      }
    }
  }

  private void updateNotification(@Nullable Song song, PlayService.State state) {
    def contentTitleString = state.toString()
    if (state == PlayService.State.Playing) {
      contentTitleString = getString(R.string.state_playing)
    } else if (state == PlayService.State.Pausing) {
      contentTitleString = getString(R.string.state_pausing)
    }
    def builder = new NotificationCompat.Builder(this)
    builder.with {
      style = new NotificationCompat.MediaStyle()
      smallIcon = R.drawable.ic_play_arrow_white_24dp
      contentTitle = contentTitleString
      contentText = song?.title
      def intent = PlayListActivity.buildIntent(this)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
      contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    startForeground(R.id.playing_songs, builder.build())
  }

}
