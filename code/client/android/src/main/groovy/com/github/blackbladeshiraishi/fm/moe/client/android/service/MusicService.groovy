package com.github.blackbladeshiraishi.fm.moe.client.android.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.MediaPlayControllerViewNotification
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.NotificationSender
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import com.github.blackbladeshiraishi.fm.moe.facade.presenter.MediaPlayControllerPresenter
import rx.android.schedulers.AndroidSchedulers

class MusicService extends Service {

  public static final String ACTION_PAUSE = "${MusicService.name}.action.PAUSE"
  public static final String ACTION_PLAY = "${MusicService.name}.action.PLAY"
  public static final String ACTION_SKIP_NEXT = "${MusicService.name}.action.SKIP_NEXT"
  public static final String ACTION_SKIP_PREVIOUS = "${MusicService.name}.action.SKIP_PREVIOUS"

  public static final String EXTRA_SONG = "extra_song"

  private MediaPlayControllerPresenter presenter

  static Intent buildIntent(Context context) {
    new Intent(context, MusicService)
  }
  static Intent buildPlaySongIntent(Context context, Song song) {
    Intent intent = buildIntent(context)
    intent.action = Intent.ACTION_VIEW
    intent.putExtra(EXTRA_SONG, song)
    return intent
  }
  static Intent buildPlayIntent(Context context) {
    buildIntent(context).setAction(ACTION_PLAY)
  }
  static Intent buildPauseIntent(Context context) {
    buildIntent(context).setAction(ACTION_PAUSE)
  }
  static Intent buildSkipNextIntent(Context context) {
    buildIntent(context).setAction(ACTION_SKIP_NEXT)
  }
  static Intent buildSkipPreviousIntent(Context context) {
    buildIntent(context).setAction(ACTION_SKIP_PREVIOUS)
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

    def controllerView = new MediaPlayControllerViewNotification(this, new NotificationSender() {
      @Override
      void send(int id, Notification notification) {
        startForeground(id, notification)
      }
    })
    presenter = new MediaPlayControllerPresenter(controllerView, AndroidSchedulers.mainThread())
    presenter.bindPlayService(MoeFmApplication.get(this).playSongComponent.playService)
  }

  @Override
  void onDestroy() {
    presenter?.unbindPlayService()//TODO unset view
    presenter = null
    MoeFmApplication.get(this).releasePlaySongComponent()
    super.onDestroy()
  }

  @Override
  int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId)
    if (intent != null) {
      switch (intent.action) {
        case Intent.ACTION_VIEW:
          Song song = intent.getSerializableExtra(EXTRA_SONG) as Song
          MoeFmApplication.get(this).playSongComponent.with {
            playList.add(song)
            if (playService.location == playList.size()) {
              playService.location = playList.size() - 1
            }
            playService.play()
          }
          break
        case ACTION_PAUSE:
          MoeFmApplication.get(this).playSongComponent.playService.pause()
          break
        case ACTION_PLAY:
          MoeFmApplication.get(this).playSongComponent.playService.play()
          break
        case ACTION_SKIP_NEXT:
          MoeFmApplication.get(this).playSongComponent.with {
            if (playService.location < playService.playList.size()) {
              playService.location = playService.location + 1
            }
          }
          break
        case ACTION_SKIP_PREVIOUS:
          MoeFmApplication.get(this).playSongComponent.with {
            if (playService.location > 0) {
              playService.location = playService.location - 1
            }
          }
          break
        default:
          new UnsupportedOperationException("unknown action: ${intent.action}")
          break
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
