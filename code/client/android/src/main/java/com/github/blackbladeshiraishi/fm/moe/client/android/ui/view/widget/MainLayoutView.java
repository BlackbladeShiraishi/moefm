package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.service.MusicService;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.StringKeys;

import flow.Flow;

public class MainLayoutView extends DrawerLayout
    implements NavigationView.OnNavigationItemSelectedListener {

  private final ViewGroup mainContainer;

  public MainLayoutView(Context context) {
    super(context);
  }

  public MainLayoutView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MainLayoutView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  // init
  {
    setId(R.id.drawer_layout);
    setFitsSystemWindows(true);

    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_main_layout, this);
    inflater.inflate(R.layout.view_main_layout_nav, this);

    mainContainer = (FrameLayout) findViewById(R.id.main_container);
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.nav_home) {
      Flow.get(this).set(MainPageView.NAME);
    } else if (id == R.id.nav_album_list) {
      Flow.get(this).set(DecoratedContentListView.ALBUM_LIST);
    } else if (id == R.id.nav_radio_list) {
      Flow.get(this).set(DecoratedContentListView.RADIO_LIST);
    } else if (id == R.id.nav_search) {
      Flow.get(this).set(SearchView.NAME);
    } else if (id == R.id.action_shutdown) {
      Flow.get(this).set(StringKeys.SHUTDOWN);
      MusicService.shutdown(getContext());
    } else {
      String selectedName = getResources().getResourceName(id);
      Toast.makeText(getContext(), "selected: " + selectedName, Toast.LENGTH_SHORT).show();
    }
    closeDrawer(GravityCompat.START);
    return true;
  }

  public void setContentView(View contentView) {
    mainContainer.removeAllViews();
    mainContainer.addView(contentView);
  }


  // ########## Input:onBackPressed ##########
  {
    // set Focusable to receive KeyEvent
    setFocusable(true);
    setFocusableInTouchMode(true);
  }

  //TODO recheck this method
  //reference: http://android-developers.blogspot.in/2009/12/back-and-other-hard-keys-three-stories.html
  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
      return super.dispatchKeyEvent(event);
    }

    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0
        ) {

      // Tell the framework to start tracking this event.
      getKeyDispatcherState().startTracking(event, this);
      return true;

    } else if (event.getAction() == KeyEvent.ACTION_UP) {
      getKeyDispatcherState().handleUpEvent(event);
      if (event.isTracking() && !event.isCanceled()) {

        // DO BACK ACTION HERE
        if (onBackPressed()) {
          return true;
        }

      }
    }
    return super.dispatchKeyEvent(event);
  }

  private boolean onBackPressed() {
    return closeDrawer();
  }

  private boolean closeDrawer() {
    if (isDrawerOpen(GravityCompat.START)) {
      closeDrawer(GravityCompat.START);
      return true;
    } else {
      return false;
    }
  }
  // ########## Input:onBackPressed End ##########

}
