package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Radio implements Serializable {

  long id

  String title

  /** key -> url map */
  Map<String, String> cover

}
