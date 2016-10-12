package com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import java.util.List;

import javax.annotation.Nonnull;

import flow.ClassKey;

//TODO check is't can contain List<Radio>
public class RadioListKey extends ClassKey {

  @Nonnull
  private final List<Radio> radioList;

  public RadioListKey(@Nonnull List<Radio> radioList) {
    this.radioList = radioList;
  }

  @Nonnull
  public List<Radio> getRadioList() {
    return radioList;
  }

}
