package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.annotation.Nullable;

import static android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Caption;
import static android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Subhead;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.blackbladeshiraishi.fm.moe.client.android.R.dimen.text_field_vertical_center_padding;


public class LabeledTextView extends LinearLayout {

  private final TextView lableView;
  private final TextView textView;

  public LabeledTextView(Context context) {
    super(context);
  }

  public LabeledTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public LabeledTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public LabeledTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    setOrientation(VERTICAL);
    lableView = new TextView(getContext());
    textView = new TextView(getContext());

    TextViewCompat.setTextAppearance(lableView, TextAppearance_AppCompat_Caption);
    lableView.setGravity(Gravity.CENTER_VERTICAL);
    lableView.setVisibility(GONE);
    int centerSpace = getResources().getDimensionPixelSize(text_field_vertical_center_padding);
    final LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
    layoutParams.bottomMargin = centerSpace;
    addView(lableView, layoutParams);

    TextViewCompat.setTextAppearance(textView, TextAppearance_AppCompat_Subhead);
    textView.setGravity(Gravity.CENTER_VERTICAL);
    addView(textView, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
  }

  public void setLable(@Nullable CharSequence text) {
    if (text == null) {
      lableView.setVisibility(GONE);
    } else {
      lableView.setText(text);
      lableView.setVisibility(VISIBLE);
    }
  }

  public void setLable(int resid) {
    lableView.setText(resid);
    lableView.setVisibility(VISIBLE);
  }

  public void setText(CharSequence text) {
    textView.setText(text);
  }

}
