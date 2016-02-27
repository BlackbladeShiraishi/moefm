package com.github.blackbladeshiraishi.fm.moe.facade.controller

import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.MoeFms

class Controller {

  final String moefmApiKey

  HotRadiosExtension hotRadiosExtension

  Controller(String moefmApiKey) {
    this.moefmApiKey = moefmApiKey
  }

  synchronized HotRadiosExtension getHotRadiosExtension() {
    if (hotRadiosExtension != null) {
      return hotRadiosExtension
    } else {
      return hotRadiosExtension =
          new HotRadiosExtension(MoeFms.newListHotRadios(MoeFms.newRetrofit(), moefmApiKey))
    }
  }

}
