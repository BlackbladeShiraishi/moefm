package com.github.blackbladeshiraishi.fm.moe.client.android.ui.controller;

import android.content.Context;
import android.view.View;

import com.github.blackbladeshiraishi.fm.moe.business.io.event.Event;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity.RadioActivity;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.facade.controller.Controller;

import javax.annotation.Nonnull;

public class AppController implements Controller {

  final Context context;

  public AppController(@Nonnull Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public void showRadio(Radio radio, Event sourceEvent) {
    if (sourceEvent.getSource() instanceof View) {
      RadioActivity.startThis(((View) sourceEvent.getSource()).getContext(), radio);
    }
  }

}
