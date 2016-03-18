package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.NotificationCompat
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.service.MusicService
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity.PlayListActivity
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import com.github.blackbladeshiraishi.fm.moe.facade.view.MediaPlayControllerView
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

import javax.annotation.Nonnull
import javax.annotation.Nullable

class MediaPlayControllerViewNotification implements MediaPlayControllerView {

  private static final int NOTIFICATION_ID = R.id.playing_songs

  private final Subject<MediaPlayControllerView.Event, MediaPlayControllerView.Event> eventBus =
      PublishSubject.create()

  private final Context context
  private final NotificationSender notificationSender

  private final NotificationCompat.Builder notificationBuilder
  private final Set<Button> buttons =
      EnumSet.of(Button.SKIP_PREVIOUS, Button.PLAY, Button.SKIP_NEXT)

  MediaPlayControllerViewNotification(Context context, NotificationSender notificationSender) {
    this.context = context.applicationContext
    notificationBuilder = new NotificationCompat.Builder(this.context)
    this.notificationSender = notificationSender
    init()
  }

  private void init() {
    notificationBuilder.with {
      style = new NotificationCompat.MediaStyle()
      smallIcon = R.drawable.ic_play_arrow_white_24dp
      def intent = PlayListActivity.buildIntent(context)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
      contentIntent =
          PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    updateButtons()
  }

  @Override
  Observable<MediaPlayControllerView.Event> eventBus() {
    return eventBus.onBackpressureDrop()
  }

  @Override
  void showSong(@Nullable Song song) {
    notificationBuilder.contentText = song?.title
    send()
  }

  @Override
  void setDuration(int duration) {
    //TODO
  }

  @Override
  void setPosition(int position) {
    //TODO
  }

  @Override
  void hideSkipNextButton() {
    buttons.remove(Button.SKIP_NEXT)
    updateButtons()
    send()
  }

  @Override
  void showSkipNextButton() {
    buttons.add(Button.SKIP_NEXT)
    updateButtons()
    send()
  }

  @Override
  void hideSkipPreviousButton() {
    buttons.remove(Button.SKIP_PREVIOUS)
    updateButtons()
    send()
  }

  @Override
  void showSkipPreviousButton() {
    buttons.add(Button.SKIP_PREVIOUS)
    updateButtons()
    send()
  }

  @Override
  void setPlayButtonState(@Nonnull MediaPlayControllerView.PlayButtonState state) {
    switch (state) {
      case MediaPlayControllerView.PlayButtonState.PLAY:
        buttons.remove(Button.PAUSE)
        buttons.add(Button.PLAY)
        notificationBuilder.contentTitle = context.getString(R.string.state_pausing)//TODO add state
        break
      case MediaPlayControllerView.PlayButtonState.PAUSE:
        buttons.remove(Button.PLAY)
        buttons.add(Button.PAUSE)
        notificationBuilder.contentTitle = context.getString(R.string.state_playing)//TODO add state
        break
      default:
        new UnsupportedOperationException("unknown state: $state")//TODO log
        return
    }
    updateButtons()
    send()
  }

  private PendingIntent getMusicService(Intent intent) {
    PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  private void updateButtons() {
    notificationBuilder.mActions.clear()//TODO
    if (buttons.contains(Button.SKIP_PREVIOUS)) {
      notificationBuilder.addAction(
          R.drawable.ic_skip_previous_white_24dp,
          context.getString(R.string.action_skip_previous),
          getMusicService(MusicService.buildSkipPreviousIntent(context))
      )
    }
    if (buttons.contains(Button.PAUSE)) {
      notificationBuilder.addAction(
          R.drawable.ic_pause_white_24dp,
          context.getString(R.string.action_pause),
          getMusicService(MusicService.buildPauseIntent(context))
      )
    }
    if (buttons.contains(Button.PLAY)) {
      notificationBuilder.addAction(
          R.drawable.ic_play_arrow_white_24dp,
          context.getString(R.string.action_play),
          getMusicService(MusicService.buildPlayIntent(context))
      )
    }
    if (buttons.contains(Button.SKIP_NEXT)) {
      notificationBuilder.addAction(
          R.drawable.ic_skip_next_white_24dp,
          context.getString(R.string.action_skip_next),
          getMusicService(MusicService.buildSkipNextIntent(context))
      )
    }
    if (!buttons.empty) {
      int[] indexes = (0..Math.min(buttons.size() - 1, 2)) as int[]
      notificationBuilder.style =
          new NotificationCompat.MediaStyle().setShowActionsInCompactView(indexes)
    } else {
      notificationBuilder.style = new NotificationCompat.MediaStyle()
    }
  }

  private void send() {
    notificationSender.send(NOTIFICATION_ID, notificationBuilder.build())
  }

  private static enum Button {

    SKIP_NEXT,
    SKIP_PREVIOUS,
    PLAY,
    PAUSE
  }
}
