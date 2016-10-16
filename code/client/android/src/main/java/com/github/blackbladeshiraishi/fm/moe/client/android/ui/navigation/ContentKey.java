package com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import javax.annotation.Nonnull;

public class ContentKey {

  @Nonnull
  private final Content content;

  public ContentKey(@Nonnull Content content) {
    this.content = content;
  }

  @Nonnull
  public Content getContent() {
    return content;
  }

  @Override
  public int hashCode() {
    long id = content.getId();
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
    return ((ContentKey)obj).content.getId() == content.getId();
  }

}
