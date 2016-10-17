package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

abstract class ContentIntroductionView extends NestedScrollView {

  public ContentIntroductionView(Context context) {
    super(context);
  }

  public ContentIntroductionView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ContentIntroductionView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  abstract void setContent(Content content);

}
