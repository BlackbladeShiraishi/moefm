package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Radio implements Serializable {

  private static final long serialVersionUID = 1L;

  long id

  String title

  List<Meta> meta

  /** key -> url map */
  Map<String, String> cover

}
