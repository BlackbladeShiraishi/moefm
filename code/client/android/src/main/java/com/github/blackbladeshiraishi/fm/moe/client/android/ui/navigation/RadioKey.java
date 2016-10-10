package com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import javax.annotation.Nonnull;

public class RadioKey {

  @Nonnull
  private final Radio radio;

  public RadioKey(@Nonnull Radio radio) {
    this.radio = radio;
  }

  @Nonnull
  public Radio getRadio() {
    return radio;
  }

  @Override
  public int hashCode() {
    long id = radio.getId();
    return (int)(id ^ (id >>> 32)); // compatible with Long.hashCode()
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    return ((RadioKey)obj).radio.getId() == radio.getId();
  }

}
