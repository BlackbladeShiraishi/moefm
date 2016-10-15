package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Radio {

  long id

  String title

  List<Meta> meta

  /** key -> url map */
  Map<String, String> cover

}
