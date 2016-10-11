package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Album implements Serializable {

  private static final long serialVersionUID = 1L;

  long id

  String title

  /** key -> url map */
  Map<String, String> cover

}
