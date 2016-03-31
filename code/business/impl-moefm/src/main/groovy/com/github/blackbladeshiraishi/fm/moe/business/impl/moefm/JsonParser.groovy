package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.entity.MoeFmMainPage
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import groovy.json.JsonSlurper
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Nonnull

// ↓ for access of JsonSlurper().parseText()
@TypeChecked(TypeCheckingMode.SKIP)
class JsonParser {

  final JsonSlurper jsonSlurper

  JsonParser(@Nonnull final JsonSlurper jsonSlurper) {
    this.jsonSlurper = jsonSlurper
  }

  MoeFmMainPage parseMainPage(String json) {
    def jsonObject = jsonSlurper.parseText(json)
    return new MoeFmMainPage(
        hotRadios: getHotRadios(jsonObject),
        newAlbums: getNewAlbums(jsonObject),
        hotAlbums: getHotAlbums(jsonObject),
        albums: getAlbums(jsonObject)
    )
  }

  private static List<Radio> getHotRadios(Object jsonObject) {
    final List<Radio> result = []
    jsonObject.response.hot_radios.each {radio ->
      result << new Radio(id: radio.wiki_id, title: radio.wiki_title, cover: radio.wiki_cover)
    }
    return result
  }

  private static List<Album> getNewAlbums(Object jsonObject) {
    final List<Album> result = []
    jsonObject.response.new_musics.each {album ->
      result << new Album(id: album.wiki_id, title: album.wiki_title, cover: album.wiki_cover)
    }
    return result
  }

  private static List<Album> getHotAlbums(Object jsonObject) {
    final List<Album> result = []
    jsonObject.response.hot_musics.each {album ->
      result << new Album(id: album.wiki_id, title: album.wiki_title, cover: album.wiki_cover)
    }
    return result
  }

  private static List<Album> getAlbums(Object jsonObject) {
    final List<Album> result = []
    jsonObject.response.musics.each {album ->
      result << new Album(id: album.wiki_id, title: album.wiki_title, cover: album.wiki_cover)
    }
    return result
  }

  List<Radio> parseHotRadios(String json) {
    final List<Radio> result = []
    jsonSlurper.parseText(json).response.hot_radios.each {rawRadio ->
      def radio = new Radio()
      radio.id = rawRadio.wiki_id
      radio.title = rawRadio.wiki_title
      result << radio
    }
    return result
  }

  List<Song> parseRadioSongs(String json) {
    final List<Song> result = []
    jsonSlurper.parseText(json).response.relationships.each {rawRelation ->
      def rawSong = rawRelation.obj
      def song = new Song(id: rawSong.sub_id, title: rawSong.sub_title, files: [])
      rawSong.sub_upload.each { rawFile ->
        song.files << new Song.File(quality: rawFile.up_quality, url: rawFile.up_url)
      }
      result << song
    }
    return result
  }

}
