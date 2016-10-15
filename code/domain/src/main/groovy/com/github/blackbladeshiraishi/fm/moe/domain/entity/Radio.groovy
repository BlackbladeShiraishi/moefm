package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Radio {

  long id

  String title

  String modifiedUserId

  List<Meta> meta

  /** key -> url map */
  Map<String, String> cover

}
