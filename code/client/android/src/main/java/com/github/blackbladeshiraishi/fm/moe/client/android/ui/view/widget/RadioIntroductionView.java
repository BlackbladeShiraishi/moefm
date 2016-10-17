package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.utils.HtmlCompat;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Meta;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.User;

import javax.annotation.Nonnull;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.blackbladeshiraishi.fm.moe.client.android.R.dimen.text_field_vertical_padding;

public class RadioIntroductionView extends ContentIntroductionView {

  private final LinearLayout fieldsContainerView;

  private final LabeledTextView titleView;
  private final LabeledTextView authorView;
  private final LabeledTextView descriptionView;

  public RadioIntroductionView(Context context) {
    super(context);
  }

  public RadioIntroductionView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RadioIntroductionView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  // init
  {
    // container views
    fieldsContainerView = new LinearLayout(getContext());
    fieldsContainerView.setOrientation(LinearLayout.VERTICAL);
    // padding
    final int hPadding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
    final int vPadding = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
    fieldsContainerView.setPadding(hPadding, vPadding, hPadding, vPadding);
    // add to this
    addView(fieldsContainerView);

    // field views
    titleView = new LabeledTextView(getContext());
    authorView = new LabeledTextView(getContext());
    descriptionView = new LabeledTextView(getContext());
    // set field views
    titleView.setLable(R.string.radio);
    authorView.setLable(R.string.author);
    descriptionView.setLable(R.string.description);
    // add to container view
    final int centerSpace = getResources().getDimensionPixelSize(text_field_vertical_padding);
    final LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
    layoutParams.bottomMargin = centerSpace;
    fieldsContainerView.addView(titleView, layoutParams);
    fieldsContainerView.addView(authorView, layoutParams);
    fieldsContainerView.addView(descriptionView);
  }

  @Override
  public void setContent(Content radio) {
    if (radio == null) {
      return;
    }
    if (!"radio".equals(radio.getType())) {
      throw new IllegalArgumentException("illegal content type: " + radio.getType());
    }
    titleView.setText(radio.getTitle());
    setAuthor(radio);
    descriptionView.setText(HtmlCompat.fromHtml(getDescription(radio)));
  }

  private void setAuthor(@Nonnull Content radio) {
    final String uid = radio.getModifiedUserId();
    if (uid == null || uid.isEmpty()) {
      authorView.setText("（无）");
      return;
    }
    authorView.setText("uid: " + uid);
    MoeFmApplication.get(getContext()).getAppComponent().getRadioService()
        .user(uid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<User>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            String message = String.format("[%s]%s", e.getClass().getSimpleName(), e.getMessage());
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
          }

          @Override
          public void onNext(User user) {
            authorView.setText(user.getNickname());
          }
        });
  }

  private static String getDescription(@Nonnull Content radio) {
    if (radio.getMeta() != null) {
      for (Meta meta : radio.getMeta()) {
        if ("简介".equals(meta.getKey())) {
          return meta.getValue();
        }
      }
    }
    return "（无）";
  }

}
