package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.api

import com.github.blackbladeshiraishi.fm.moe.business.api.RadioService
import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import com.github.blackbladeshiraishi.fm.moe.domain.entity.User
import groovy.json.JsonSlurper
import rx.Observable

import javax.annotation.Nonnull
import javax.annotation.Nullable

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
  Observable<MoeFmMainPage> mainPage() {
    return moeFmService.mainPage(apiKey)
        .map {jsonParser.parseMainPage(it?.string())}
  }

  @Override
  Observable<Radio> hotRadios() {
    return moeFmService.hotRadios(apiKey)
        .map {jsonParser.parseHotRadios(it?.string())}
        .flatMap{Observable.from(it)}
  }

  @Override
  Observable<List<Content>> searchContents(@Nonnull String keyword, @Nullable String type) {
    return moeFouService.searchContents(apiKey, keyword, type)
        .map {jsonParser.parseContents(it?.string())}
  }

  @Override
  Observable<Radio> radios() {
    return moeFouService.radios(apiKey)
        .map {jsonParser.parseRadios(it?.string())}
        .flatMap{Observable.from(it)}
  }

  @Override
  Observable<Song> radioSongs(@Nonnull Radio radio) {
    return moeFouService.radioSongs(apiKey, radio.id)
        .map {jsonParser.parseRadioSongs(it?.string())}
        .flatMap{Observable.from(it)}
  }

  @Override
  Observable<Album> albums() {
    return moeFouService.albums(apiKey)
        .map {jsonParser.parseAlbums(it?.string())}
        .flatMap{Observable.from(it)}
  }

  @Override
  Observable<Song> albumSongs(@Nonnull Album album) {
    return moeFouService.albumSongs(apiKey, album.id)
        .map {jsonParser.parseAlbumSongs(it?.string())}
        .flatMap{Observable.from(it)}
  }

  @Override
  Observable<User> user(String uid) {
    return moeFouService.user(apiKey, uid)
        .map { jsonParser.parseUser(it.string()) }
  }

}
