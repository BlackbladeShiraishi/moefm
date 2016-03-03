package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Song implements Serializable {

  long id
  String title
  List<File> files

  static class File implements Serializable {
    String quality
    String url
  }

}
