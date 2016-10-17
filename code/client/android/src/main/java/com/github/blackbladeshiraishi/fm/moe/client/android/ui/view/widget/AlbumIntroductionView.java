package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.utils.HtmlCompat;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Meta;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.blackbladeshiraishi.fm.moe.client.android.R.dimen.text_field_vertical_padding;


public class AlbumIntroductionView extends ContentIntroductionView {

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
    final int hPadding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
    final int vPadding = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
    fieldsContainerView.setPadding(hPadding, vPadding, hPadding, vPadding);

    addView(fieldsContainerView);
  }

  @Override
  public void setContent(Content album) {
    if (album == null || album.getMeta() == null) {
      fieldsContainerView.removeAllViews();
      return;
    }
    if (!"music".equals(album.getType())) {
      throw new IllegalArgumentException("illegal content type: " + album.getType());
    }
    final List<Meta> metaList = album.getMeta();
    final int centerSpace = getResources().getDimensionPixelSize(text_field_vertical_padding);
    Observable.from(metaList).skipLast(1).subscribe(new Action1<Meta>() {
      @Override
      public void call(Meta meta) {
        final LinearLayout.LayoutParams layoutParams =
            new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.bottomMargin = centerSpace;
        fieldsContainerView.addView(newFieldView(meta), layoutParams);
      }
    });
    fieldsContainerView.addView(newFieldView(metaList.get(metaList.size() - 1)));
  }

  private View newFieldView(Meta meta) {
    LabeledTextView labeledTextView = new LabeledTextView(getContext());
    labeledTextView.setLable(meta.getKey());
    labeledTextView.setText(HtmlCompat.fromHtml(meta.getValue()));
    return labeledTextView;
  }

}
