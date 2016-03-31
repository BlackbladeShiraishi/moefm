package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Album implements Serializable {

  long id

  String title

  /** key -> url map */
  Map<String, String> cover

}
