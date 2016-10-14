package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RadioView extends FrameLayout {

  private final SongListView radioSongListView;
  private final TextView radioTitleView;
  private final TextView radioAuthorView;
  private final TextView radioDescriptionView;

  private Radio radio;

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
    List<Tab> tabs = new ArrayList<>(2);
    // introduction
    View radioIntroductionView = inflater.inflate(R.layout.view_radio_introduction, this, false);
    tabs.add(new Tab("简介", radioIntroductionView));
    radioTitleView = (TextView) radioIntroductionView.findViewById(R.id.title_view);
    radioAuthorView = (TextView) radioIntroductionView.findViewById(R.id.author_view);
    radioDescriptionView = (TextView) radioIntroductionView.findViewById(R.id.description_view);
    // song list
    radioSongListView = new SongListView(getContext());
    tabs.add(new Tab("曲目", radioSongListView));

    tabsView.setAdapter(new TabAdapter(tabs));
  }

  public void setRadio(Radio radio) {
    this.radio = radio;
  }

  public void refresh() {
    if (radio == null) {
      //TODO show message
      return;
    }
    radioTitleView.setText(radio.getTitle());
    radioAuthorView.setText(radio.getTitle());
    radioDescriptionView.setText(radio.getTitle());
    MoeFmApplication.get(getContext()).getAppComponent().getRadioService().radioSongs(radio)
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

  private static class TabAdapter extends PagerAdapter {

    final List<Tab> tabs;

    private TabAdapter(List<Tab> tabs) {
      this.tabs = tabs;
    }

    @Override
    public int getCount() {
      return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return tabs.get(position).title;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      final View view = tabs.get(position).view;
      container.addView(view);
      return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      // do nothing //TODO check it
    }

  }

  private static class Tab {
    final String title;
    final View view;

    public Tab(String title, View view) {
      this.title = title;
      this.view = view;
    }
  }

}
