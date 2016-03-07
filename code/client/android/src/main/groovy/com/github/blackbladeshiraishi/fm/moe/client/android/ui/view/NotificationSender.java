package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view;

import android.app.Notification;

public interface NotificationSender {

  void send(int id, Notification notification);

}
