package com.github.blackbladeshiraishi.fm.moe.domain.entity

class Song implements Serializable {

  private static final long serialVersionUID = 1L;

  long id
  String title
  List<File> files

  static class File implements Serializable {
    private static final long serialVersionUID = 1L;
    String quality
    String url
  }

}
