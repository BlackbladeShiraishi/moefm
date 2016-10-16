package com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import javax.annotation.Nonnull;

public class RadioKey {

  @Nonnull
  private final Content radio;

  public RadioKey(@Nonnull Content radio) {
    this.radio = radio;
  }

  @Nonnull
  public Content getRadio() {
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
