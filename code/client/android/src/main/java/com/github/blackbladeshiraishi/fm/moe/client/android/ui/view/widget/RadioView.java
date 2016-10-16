package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.TabAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.utils.HtmlCompat;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Meta;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RadioView extends FrameLayout {

  private final SongListView radioSongListView;
  private final TextView radioTitleView;
  private final TextView radioAuthorView;
  private final TextView radioDescriptionView;

  private Content radio;

  public RadioView(Context context) {
    super(context);
  }

  public RadioView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RadioView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public RadioView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_radio, this);
    ViewPager tabsView = (ViewPager) findViewById(R.id.tabs_view);

    // Tab Views
    List<TabAdapter.Tab> tabs = new ArrayList<>(2);
    // introduction
    View radioIntroductionView = inflater.inflate(R.layout.view_radio_introduction, this, false);
    tabs.add(new TabAdapter.Tab("简介", radioIntroductionView));
    radioTitleView = (TextView) radioIntroductionView.findViewById(R.id.title_view);
    radioAuthorView = (TextView) radioIntroductionView.findViewById(R.id.author_view);
    radioDescriptionView = (TextView) radioIntroductionView.findViewById(R.id.description_view);
    // song list
    radioSongListView = new SongListView(getContext());
    tabs.add(new TabAdapter.Tab("曲目", radioSongListView));

    tabsView.setAdapter(new TabAdapter(tabs));
  }

  public void setRadio(Content radio) {
    this.radio = radio;
  }

  public void refresh() {
    if (radio == null) {
      //TODO show message
      return;
    }
    radioTitleView.setText(radio.getTitle());
    refreshAuthor();
    radioDescriptionView.setText(HtmlCompat.fromHtml(getDescription(radio)));
    MoeFmApplication.get(getContext()).getAppComponent().getRadioService().radioSongs(radio.getId())
        .subscribeOn(Schedulers.io())
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Song>>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            String message = String.format("[%s]%s", e.getClass().getSimpleName(), e.getMessage());
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
          }

          @Override
          public void onNext(List<Song> songList) {
            radioSongListView.setSongList(songList);
          }
        });
  }

  private void refreshAuthor() {
    final String uid = radio.getModifiedUserId();
    if (uid == null || uid.isEmpty()) {
      radioAuthorView.setText("（无）");
      return;
    }
    radioAuthorView.setText("uid: " + uid);
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
            radioAuthorView.setText(user.getNickname());
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
