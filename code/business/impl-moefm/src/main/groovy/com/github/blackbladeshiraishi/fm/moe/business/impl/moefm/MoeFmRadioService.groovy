package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

import com.github.blackbladeshiraishi.fm.moe.business.business.RadioService
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import groovy.json.JsonSlurper
import retrofit2.Retrofit
import rx.Observable

import javax.annotation.Nonnull

class MoeFmRadioService implements RadioService {

  private final JsonParser jsonParser = new JsonParser(new JsonSlurper())

  final String apiKey

  final MoeFmService moeFmService

  MoeFmRadioService(@Nonnull final Retrofit retrofit, @Nonnull final String apiKey) {
    assert apiKey != null
    this.apiKey = apiKey
    moeFmService = retrofit.create(MoeFmService)
  }

  @Override
  Observable<Radio> hotRadios() {
    return moeFmService.hotRadios(apiKey)
        .map{jsonParser.parseHotRadios(it.string())}
        .flatMap{Observable.from(it)}
  }

  @Override
  Observable<Song> radioSongs(Radio radio) {
    return moeFmService.radioSongs(apiKey, radio.id)
        .map{jsonParser.parseRadioSongs(it.string())}
        .flatMap{Observable.from(it)}
  }

}
