package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

import com.github.blackbladeshiraishi.fm.moe.business.api.RadioService
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import groovy.json.JsonSlurper
import rx.Observable

import javax.annotation.Nonnull

class MoeFmRadioService implements RadioService {

  private final JsonParser jsonParser = new JsonParser(new JsonSlurper())

  final String apiKey

  final MoeFmService moeFmService

  final MoeFouService moeFouService

  MoeFmRadioService(
      @Nonnull final MoeFmService moeFmService,
      @Nonnull final MoeFouService moeFouService,
      @Nonnull final String apiKey) {
    assert apiKey != null
    this.apiKey = apiKey
    this.moeFmService = moeFmService
    this.moeFouService = moeFouService
  }

  @Override
  Observable<Radio> hotRadios() {
    return moeFmService.hotRadios(apiKey)
        .map{jsonParser.parseHotRadios(it.string())}
        .flatMap{Observable.from(it)}
  }

  @Override
  Observable<Song> radioSongs(Radio radio) {
    return moeFouService.radioSongs(apiKey, radio.id)
        .map{jsonParser.parseRadioSongs(it.string())}
        .flatMap{Observable.from(it)}
  }

}
