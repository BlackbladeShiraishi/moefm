package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Song {

  long id
  String title
  List<File> files

  static class File {
    String quality
    String url
  }

}
