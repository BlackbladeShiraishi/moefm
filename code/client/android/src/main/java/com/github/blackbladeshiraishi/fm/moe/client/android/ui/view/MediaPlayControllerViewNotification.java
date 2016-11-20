package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.github.blackbladeshiraishi.fm.moe.business.api.RadioService;
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.service.MusicService;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity.PlayListActivity;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;
import com.github.blackbladeshiraishi.fm.moe.facade.view.BaseView;
import com.github.blackbladeshiraishi.fm.moe.facade.view.MediaPlayControllerView;
import com.github.blackbladeshiraishi.fm.moe.facade.view.View;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class MediaPlayControllerViewNotification
        extends BaseView<MediaPlayControllerView, View.Event<MediaPlayControllerView>>
        implements MediaPlayControllerView {

  private static final int NOTIFICATION_ID = R.id.playing_songs;

  private final Context context;
  private final NotificationSender notificationSender;

  private final NotificationCompat.Builder notificationBuilder;
  private final Set<Button> buttons =
      EnumSet.of(Button.SKIP_PREVIOUS, Button.PLAY, Button.SKIP_NEXT);

  private final BehaviorSubject<Song> songSubject = BehaviorSubject.create();

  public MediaPlayControllerViewNotification(Context context, NotificationSender notificationSender) {
    this.context = context.getApplicationContext();
    notificationBuilder = new NotificationCompat.Builder(this.context);
    this.notificationSender = notificationSender;
    init();
  }

  private void init() {
    final Intent intent = PlayListActivity.buildIntent(context)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    notificationBuilder
        .setStyle(new NotificationCompat.MediaStyle())
        .setSmallIcon(R.drawable.ic_play_arrow_white_24dp)
        .setContentIntent(
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    updateButtons();

    bindSongObservable();
  }

  private void bindSongObservable() {
    RadioService radioService = MoeFmApplication.get(context).getAppComponent().getRadioService();
    Picasso picasso = Picasso.with(context);
    songSubject
        .observeOn(Schedulers.io())
        .filter(song -> song != null)
        .debounce(100, TimeUnit.MILLISECONDS)
        .switchMap(song ->
                       radioService.albumDetail(song.getAlbumId())
                           .onErrorResumeNext(Observable.empty())
                           .map(MediaPlayControllerViewNotification::selectCover)
                           .map(url -> {
                             try {
                               return picasso.load(url).get();//TODO use cache
                             } catch (IOException e) {
                               return null;
                             }
                           })
        )
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(bitmap -> {
          notificationBuilder.setLargeIcon(bitmap);
          send();
        });
  }

  @Override
  public void showSong(@Nullable Song song) {
    songSubject.onNext(song);
    notificationBuilder.setContentText(song == null ? null : song.getTitle());
    send();
  }

  @Nullable
  private static String selectCover(@Nullable Content album) {
    final String[] COVER_KEY = {"square", "small", "medium", "large"};
    if (album == null) {
      return null;
    }
    final Map<String, String> cover = album.getCover();
    String result = null;
    for (String key : COVER_KEY) {
      result = cover.get(key);
      if (result != null) {
        break;
      }
    }
    return result;
  }

  @Override
  public void setDuration(int duration) {
    //TODO
  }

  @Override
  public void setPosition(int position) {
    //TODO
  }

  @Override
  public void hideSkipNextButton() {
    buttons.remove(Button.SKIP_NEXT);
    updateButtons();
    send();
  }

  @Override
  public void showSkipNextButton() {
    buttons.add(Button.SKIP_NEXT);
    updateButtons();
    send();
  }

  @Override
  public void hideSkipPreviousButton() {
    buttons.remove(Button.SKIP_PREVIOUS);
    updateButtons();
    send();
  }

  @Override
  public void showSkipPreviousButton() {
    buttons.add(Button.SKIP_PREVIOUS);
    updateButtons();
    send();
  }

  @Override
  public void setPlayButtonState(@Nonnull MediaPlayControllerView.PlayButtonState state) {
    switch (state) {
      case PLAY:
        buttons.remove(Button.PAUSE);
        buttons.add(Button.PLAY);
        notificationBuilder.setContentTitle(context.getString(R.string.state_pausing));//TODO add state
        break;
      case PAUSE:
        buttons.remove(Button.PLAY);
        buttons.add(Button.PAUSE);
        notificationBuilder.setContentTitle(context.getString(R.string.state_playing));//TODO add state
        break;
      default:
        new UnsupportedOperationException("unknown state: " + state).printStackTrace();//TODO log
        return;
    }
    updateButtons();
    send();
  }

  private PendingIntent getMusicService(Intent intent) {
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private void updateButtons() {
    notificationBuilder.mActions.clear();//TODO
    if (buttons.contains(Button.SKIP_PREVIOUS)) {
      notificationBuilder.addAction(
          R.drawable.ic_skip_previous_white_24dp,
          context.getString(R.string.action_skip_previous),
          getMusicService(MusicService.buildSkipPreviousIntent(context))
      );
    }
    if (buttons.contains(Button.PAUSE)) {
      notificationBuilder.addAction(
          R.drawable.ic_pause_white_24dp,
          context.getString(R.string.action_pause),
          getMusicService(MusicService.buildPauseIntent(context))
      );
    }
    if (buttons.contains(Button.PLAY)) {
      notificationBuilder.addAction(
          R.drawable.ic_play_arrow_white_24dp,
          context.getString(R.string.action_play),
          getMusicService(MusicService.buildPlayIntent(context))
      );
    }
    if (buttons.contains(Button.SKIP_NEXT)) {
      notificationBuilder.addAction(
          R.drawable.ic_skip_next_white_24dp,
          context.getString(R.string.action_skip_next),
          getMusicService(MusicService.buildSkipNextIntent(context))
      );
    }
    if (!buttons.isEmpty()) {
      // int[] indexes = (0..Math.min(buttons.size() - 1, 2)) as int[]
      final int upperBound = Math.min(buttons.size() - 1, 2);
      final int[] indexes = new int[upperBound + 1];
      for (int i = 0; i <= upperBound; i++) {
        indexes[i] = i;
      }
      notificationBuilder.setStyle(
          new NotificationCompat.MediaStyle().setShowActionsInCompactView(indexes));
    } else {
      notificationBuilder.setStyle(new NotificationCompat.MediaStyle());
    }
  }

  private void send() {
    notificationSender.send(NOTIFICATION_ID, notificationBuilder.build());
  }

  private static enum Button {

    SKIP_NEXT,
    SKIP_PREVIOUS,
    PLAY,
    PAUSE
  }
}
