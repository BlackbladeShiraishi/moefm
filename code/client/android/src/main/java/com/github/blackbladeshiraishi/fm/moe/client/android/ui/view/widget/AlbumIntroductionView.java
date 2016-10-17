package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.utils.HtmlCompat;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Meta;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.blackbladeshiraishi.fm.moe.client.android.R.dimen.text_field_vertical_padding;


public class AlbumIntroductionView extends ContentIntroductionView {

  private final int centerSpace;

  private final LinearLayout fieldsContainerView;

  public AlbumIntroductionView(Context context) {
    super(context);
  }

  public AlbumIntroductionView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AlbumIntroductionView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  // init
  {
    fieldsContainerView = new LinearLayout(getContext());
    fieldsContainerView.setOrientation(LinearLayout.VERTICAL);
    // padding
    centerSpace = getResources().getDimensionPixelSize(text_field_vertical_padding);
    final int hPadding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
    int vPadding = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
    vPadding = vPadding - centerSpace / 2;
    fieldsContainerView.setPadding(hPadding, vPadding, hPadding, vPadding);

    addView(fieldsContainerView);
  }

  @Override
  public void setContent(Content album) {
    fieldsContainerView.removeAllViews();
    if (album == null) {
      return;
    }
    addField("专辑", album.getTitle());
    if (album.getMeta() != null) {
      for (Meta meta : album.getMeta()) {
        addField(meta.getKey(), HtmlCompat.fromHtml(meta.getValue()));
      }
    }
  }

  private void addField(CharSequence label, CharSequence text) {
    LabeledTextView labeledTextView = new LabeledTextView(getContext());
    labeledTextView.setLable(label);
    labeledTextView.setText(text);
    // set center space with layout params
    final LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
    layoutParams.topMargin = centerSpace / 2;
    layoutParams.bottomMargin = centerSpace / 2;
    fieldsContainerView.addView(labeledTextView, layoutParams);
  }

}
