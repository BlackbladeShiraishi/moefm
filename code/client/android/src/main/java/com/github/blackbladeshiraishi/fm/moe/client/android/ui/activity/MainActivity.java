package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.AlbumListKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.ContentKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.RadioListKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.StringKeys;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.ContentListView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.ContentView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.DecoratedContentListView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.MainLayoutView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.MainPageView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.SearchView;

import java.util.Map;

import flow.Direction;
import flow.Flow;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.State;
import flow.TraversalCallback;

public class MainActivity extends AppCompatActivity {

  private MainLayoutView layoutView;
  private View contentView;

  // ##### flow #####
  @Override
  protected void attachBaseContext(Context newBase) {
    newBase = Flow.configure(newBase, this)
        .dispatcher(KeyDispatcher.configure(this, new MainKeyChanger()).build())
        .defaultKey(MainPageView.NAME)
        .install();
    super.attachBaseContext(newBase);
  }

  @Override
  public void onBackPressed() {
    if (!Flow.get(this).goBack()) {
      super.onBackPressed();
    }
  }
  // ##### flow end #####


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    layoutView = new MainLayoutView(this);
    setContentView(layoutView);
  }

  private class MainKeyChanger implements KeyChanger {
    @Override
    public void changeKey(@Nullable State outgoingState, @NonNull State incomingState,
                          @NonNull Direction direction,
                          @NonNull Map<Object, Context> incomingContexts,
                          @NonNull TraversalCallback callback) {
      // save outgoingState
      if (outgoingState != null && contentView != null) {
        outgoingState.save(contentView);
      }
      // transit to new state
      final Object incomingKey = incomingState.getKey();
      final Context incomingContext = incomingContexts.get(incomingKey);
      if (incomingKey.equals(StringKeys.SHUTDOWN)) {
        finish();
      } else if (incomingKey.equals(MainPageView.NAME)) {
        contentView = new MainPageView(incomingContext);
      } else if (incomingKey.equals(SearchView.NAME)) {
        contentView = new SearchView(incomingContext);
      } else if (incomingKey.equals(DecoratedContentListView.RADIO_LIST) ||
                 incomingKey.equals(DecoratedContentListView.ALBUM_LIST)) {
        contentView = new DecoratedContentListView(incomingContext);
      } else if (incomingKey.getClass().equals(AlbumListKey.class)) {
        ContentListView contentListView = new ContentListView(incomingContext);
        contentListView.setContent(((AlbumListKey) incomingKey).getAlbumList());
        contentView = contentListView;
      } else if (incomingKey.getClass().equals(RadioListKey.class)) {
        ContentListView contentListView = new ContentListView(incomingContext);
        contentListView.setContent(((RadioListKey) incomingKey).getRadioList());
        contentView = contentListView;
      } else if (incomingKey.getClass().equals(ContentKey.class)) {
        ContentView radioContentView = new ContentView(incomingContext);
        radioContentView.setContent(((ContentKey) incomingKey).getContent());
        radioContentView.refresh();
        contentView = radioContentView;
      } else {
        final String message = String.format(
            "[%s]%s is under construct", incomingKey.getClass().getSimpleName(), incomingKey);
        Toast.makeText(incomingContext, message, Toast.LENGTH_LONG).show();
      }
      if (contentView != null) {
        layoutView.setContentView(contentView);
        incomingState.restore(contentView);
      }
      callback.onTraversalCompleted();
    }
  }

}
