package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

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
