package com.github.blackbladeshiraishi.fm.moe.facade.controller;

import com.github.blackbladeshiraishi.fm.moe.business.io.event.Event;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

public interface Controller {

  void showRadio(Radio radio, Event sourceEvent);

}
