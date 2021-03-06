package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.api

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MoeFmMainPage
import com.github.blackbladeshiraishi.fm.moe.domain.entity.*
import groovy.json.JsonSlurper
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Nonnull
import javax.annotation.Nullable

// ↓ for access of JsonSlurper().parseText()
@TypeChecked(TypeCheckingMode.SKIP)
class JsonParser {

  final JsonSlurper jsonSlurper

  JsonParser(@Nonnull final JsonSlurper jsonSlurper) {
    this.jsonSlurper = jsonSlurper
  }

  MoeFmMainPage parseMainPage(@Nullable String json) {
    if (json == null) {
      return new MoeFmMainPage(
          hotRadios: Collections.emptyList(),
          newAlbums: Collections.emptyList(),
          hotAlbums: Collections.emptyList(),
          albums: Collections.emptyList()
      )
    }
    def jsonObject = jsonSlurper.parseText(json)
    return new MoeFmMainPage(
        hotRadios: getHotRadios(jsonObject),
        newAlbums: getNewAlbums(jsonObject),
        hotAlbums: getHotAlbums(jsonObject),
        albums: getAlbums(jsonObject)
    )
  }

  private static List<Content> getHotRadios(@Nonnull Object jsonObject) {
    final List<Content> result = []
    jsonObject.response.hot_radios.each {radio ->
      result << parseContentWiki(radio)
    }
    return result
  }

  private static List<Content> getNewAlbums(@Nonnull Object jsonObject) {
    final List<Content> result = []
    jsonObject.response.new_musics.each {album ->
      result << parseContentWiki(album)
    }
    return result
  }

  private static List<Content> getHotAlbums(@Nonnull Object jsonObject) {
    final List<Content> result = []
    jsonObject.response.hot_musics.each {album ->
      result << parseContentWiki(album)
    }
    return result
  }

  private static List<Content> getAlbums(@Nonnull Object jsonObject) {
    final List<Content> result = []
    jsonObject.response.musics.each {album ->
      result << parseContentWiki(album)
    }
    return result
  }

  @Nonnull
  List<Content> parseContents(@Nullable String json) {
    final List<Content> result = []
    if (json != null) {
      jsonSlurper.parseText(json).response.wikis.each { rawWiki ->
        result << parseContentWiki(rawWiki)
      }
    }
    return result;
  }

  @Nonnull
  Content parseContentDetail(@Nonnull String json) {
    parseContentWiki(jsonSlurper.parseText(json).response.wiki)
  }

  @Nonnull
  private static Content parseContentWiki(@Nonnull def contentWiki) {
    return new Content(
            id: contentWiki.wiki_id,
            type: contentWiki.wiki_type,
            title: contentWiki.wiki_title,
            modifiedUserId: contentWiki.wiki_modified_user,
            meta: parseMetaList(contentWiki.wiki_meta),
            cover: new HashMap<>(contentWiki.wiki_cover as Map)
    )
  }

  @Nullable
  private static List<Meta> parseMetaList(@Nullable def metaList) {
    if (metaList == null) {
      return null
    }
    final List<Meta> result = []
    metaList.each { result << parseMeta(it) }
    return result
  }

  private static Meta parseMeta(@Nonnull def meta) {
    return new Meta(key: meta.meta_key, type: meta.meta_type, value: meta.meta_value)
  }

  @Nonnull
  List<Song> parseRadioSongs(@Nullable String json) {
    if (json == null) {
      return Collections.emptyList()
    }
    final List<Song> result = []
    jsonSlurper.parseText(json).response.relationships.each {rawRelation ->
      if (rawRelation.obj != null) {
        result << parseSubItemSong(rawRelation.obj)
      }
    }
    return result
  }

  @Nonnull
  List<Song> parseAlbumSongs(@Nullable String json) {
    if (json == null) {
      return Collections.emptyList()
    }
    final List<Song> result = []
    jsonSlurper.parseText(json).response.subs.each {rawSubItemSong ->
      result << parseSubItemSong(rawSubItemSong)
    }
    return result
  }

  @Nonnull
  private static Song parseSubItemSong(@Nonnull def rawSong) {
    def song = new Song(id: rawSong.sub_id, albumId: rawSong.sub_parent_wiki, title: rawSong.sub_title, files: [])
    rawSong.sub_upload.each { rawFile ->
      song.files << new Song.File(quality: rawFile.up_quality, url: rawFile.up_url)
    }
    return song;
  }

  @Nonnull
  User parseUser(@Nonnull String json) {
    def user = jsonSlurper.parseText(json).response.user
    return new User(
            uid: user.uid,
            nickname: user.user_nickname,
            avatar: new HashMap<>(user.user_avatar)
    )
  }

}
