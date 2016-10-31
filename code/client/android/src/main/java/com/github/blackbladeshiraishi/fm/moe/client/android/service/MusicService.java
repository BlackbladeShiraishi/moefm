package com.github.blackbladeshiraishi.fm.moe.client.android.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.github.blackbladeshiraishi.fm.moe.business.api.PlayList;
import com.github.blackbladeshiraishi.fm.moe.business.api.PlayService;
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.inject.PlaySongComponent;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.MediaPlayControllerViewNotification;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.NotificationSender;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;
import com.github.blackbladeshiraishi.fm.moe.facade.presenter.MediaPlayControllerPresenter;
import rx.android.schedulers.AndroidSchedulers;

public class MusicService extends Service {

  private static final String ACTION_PREFIX = "com.github.blackbladeshiraishi.fm.moe.intent.action.";
  public static final String ACTION_PAUSE = ACTION_PREFIX + "PAUSE";
  public static final String ACTION_PLAY = ACTION_PREFIX + "PLAY";
  public static final String ACTION_SKIP_NEXT = ACTION_PREFIX + "SKIP_NEXT";
  public static final String ACTION_SKIP_PREVIOUS = ACTION_PREFIX + "SKIP_PREVIOUS";
  public static final String ACTION_SHUTDOWN = ACTION_PREFIX + "SHUTDOWN";

  public static final String EXTRA_SONG = "extra_song";

  private MediaPlayControllerPresenter presenter;

  static Intent buildIntent(Context context) {
    return new Intent(context, MusicService.class);
  }
  static Intent buildPlaySongIntent(Context context, Song song) {
    Intent intent = buildIntent(context);
    intent.setAction(Intent.ACTION_VIEW);
    intent.putExtra(EXTRA_SONG, song);
    return intent;
  }
  public static Intent buildPlayIntent(Context context) {
    return buildIntent(context).setAction(ACTION_PLAY);
  }
  public static Intent buildPauseIntent(Context context) {
    return buildIntent(context).setAction(ACTION_PAUSE);
  }
  public static Intent buildSkipNextIntent(Context context) {
    return buildIntent(context).setAction(ACTION_SKIP_NEXT);
  }
  public static Intent buildSkipPreviousIntent(Context context) {
    return buildIntent(context).setAction(ACTION_SKIP_PREVIOUS);
  }
  public static Intent buildShutdownIntent(Context context) {
    return buildIntent(context).setAction(ACTION_SHUTDOWN);
  }

  public static void playSong(Context context, Song song) {
    context.startService(buildPlaySongIntent(context, song));
  }

  public static void shutdown(Context context) {
    context.startService(buildShutdownIntent(context));
  }

  public MusicService() {
  }

  @Override
  public void onCreate() {
    super.onCreate();
    MoeFmApplication.get(this).createPlaySongComponent();

    MediaPlayControllerViewNotification controllerView = new MediaPlayControllerViewNotification(this, new NotificationSender() {
      @Override
      public void send(int id, Notification notification) {
        startForeground(id, notification);
      }
    });
    presenter = new MediaPlayControllerPresenter(controllerView, AndroidSchedulers.mainThread());
    presenter.bindPlayService(MoeFmApplication.get(this).getPlaySongComponent().getPlayService());
  }

  @Override
  public void onDestroy() {
    if (presenter != null) {
      presenter.unbindPlayService();//TODO unset view
      presenter = null;
    }
    MoeFmApplication.get(this).releasePlaySongComponent();
    super.onDestroy();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    if (intent != null) {
      switch (intent.getAction()) {
        case Intent.ACTION_VIEW:
          Song song = (Song) intent.getSerializableExtra(EXTRA_SONG);
        {
          final PlaySongComponent playSongComponent = MoeFmApplication.get(this).getPlaySongComponent();
          final PlayList playList = playSongComponent.getPlayList();
          final PlayService playService = playSongComponent.getPlayService();
          playList.add(song);
          if (playService.getLocation() == playList.size()) {
            playService.setLocation(playList.size() - 1);
          }
          playService.play();
        }
          break;
        case ACTION_PAUSE:
          MoeFmApplication.get(this).getPlaySongComponent().getPlayService().pause();
          break;
        case ACTION_PLAY:
          MoeFmApplication.get(this).getPlaySongComponent().getPlayService().play();
          break;
        case ACTION_SKIP_NEXT:
        {
          PlayService playService = MoeFmApplication.get(this).getPlaySongComponent().getPlayService();
          if (playService.getLocation() < playService.getPlayList().size()) {
            playService.setLocation(playService.getLocation() + 1);
          }
        }
          break;
        case ACTION_SKIP_PREVIOUS:
        {
          PlayService playService = MoeFmApplication.get(this).getPlaySongComponent().getPlayService();
          if (playService.getLocation() > 0) {
            playService.setLocation(playService.getLocation() - 1);
          }
        }
          break;
        case ACTION_SHUTDOWN:
        {
          PlayService playService = MoeFmApplication.get(this).getPlaySongComponent().getPlayService();
          playService.close();
          stopForeground(true);
          stopSelf();
        }
          break;
        default:
          new UnsupportedOperationException("unknown action: " + intent.getAction()).printStackTrace();
          break;
      }
    } else {
      //TODO
    }
    return START_STICKY; //TODO selfStop
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }


}
