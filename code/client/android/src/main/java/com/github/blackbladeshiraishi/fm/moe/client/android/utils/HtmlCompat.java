package com.github.blackbladeshiraishi.fm.moe.client.android.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.os.BuildCompat;
import android.text.Html;
import android.text.Spanned;

public class HtmlCompat {

  private static final HtmlCompatImpl IMPL;
  static {
    if (BuildCompat.isAtLeastN()) {
      IMPL = new Api24HtmlCompatImpl();
    } else {
      IMPL = new BaseHtmlCompatImpl();
    }
  }

  public static Spanned fromHtml(String source) {
    return IMPL.fromHtml(source);
  }

  private interface HtmlCompatImpl {
    Spanned fromHtml(String source);
  }

  private static class BaseHtmlCompatImpl implements HtmlCompatImpl {
    @Override
    @SuppressWarnings("deprecation")
    public Spanned fromHtml(String source) {
      return Html.fromHtml(source);
    }
  }

  @TargetApi(Build.VERSION_CODES.N)
  private static class Api24HtmlCompatImpl implements HtmlCompatImpl {
    @Override
    public Spanned fromHtml(String source) {
      return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }
  }

}
