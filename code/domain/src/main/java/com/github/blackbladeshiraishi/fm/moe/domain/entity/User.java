package com.github.blackbladeshiraishi.fm.moe.domain.entity;

import java.util.Map;

public class User {

  private String uid;

  private String nickname;

  private Map<String, String> avatar;

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Map<String, String> getAvatar() {
    return avatar;
  }

  public void setAvatar(Map<String, String> avatar) {
    this.avatar = avatar;
  }

}
