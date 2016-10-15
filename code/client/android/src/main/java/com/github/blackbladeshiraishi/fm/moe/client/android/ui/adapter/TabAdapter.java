package com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TabAdapter extends PagerAdapter {

  final List<Tab> tabs;

  public TabAdapter(List<Tab> tabs) {
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

  public static class Tab {
    final String title;
    final View view;

    public Tab(String title, View view) {
      this.title = title;
      this.view = view;
    }
  }
}
