package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
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
    List<Radio> result = []
    jsonSlurper.parseText(json).response.hot_radios.each {rawRadio ->
      def radio = new Radio()
      radio.id = rawRadio.wiki_id
      radio.title = rawRadio.wiki_title
      result << radio
    }
    return result
  }

}
