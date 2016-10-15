package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Album {

  long id

  String title

  List<Meta> meta

  /** key -> url map */
  Map<String, String> cover

}
