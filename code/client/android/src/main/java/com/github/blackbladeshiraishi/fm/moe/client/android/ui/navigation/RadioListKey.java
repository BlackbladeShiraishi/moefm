package com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.List;

import javax.annotation.Nonnull;

import flow.ClassKey;

//TODO check is't can contain List<Content>
public class RadioListKey extends ClassKey {

  @Nonnull
  private final List<Content> radioList;

  public RadioListKey(@Nonnull List<Content> radioList) {
    this.radioList = radioList;
  }

  @Nonnull
  public List<Content> getRadioList() {
    return radioList;
  }

}
