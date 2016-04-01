package com.github.blackbladeshiraishi.fm.moe.facade.view.event

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio

public class ShowRadioEvent implements UiEvent {

  final Object source

  final Radio radio

  ShowRadioEvent(Object source, Radio radio) {
    this.source = source
    this.radio = radio
  }

}
