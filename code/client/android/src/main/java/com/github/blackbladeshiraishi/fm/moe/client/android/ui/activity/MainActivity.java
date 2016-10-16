package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.AlbumListKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.ContentKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.RadioListKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.AlbumView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.ContentListView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.MainLayoutView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.MainPageView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.RadioView;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.SearchView;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;

import java.util.Map;

import flow.Direction;
import flow.Flow;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.State;
import flow.TraversalCallback;

public class MainActivity extends AppCompatActivity {

  private MainLayoutView layoutView;

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
      final Object incomingKey = incomingState.getKey();
      final Context incomingContext = incomingContexts.get(incomingKey);
      if (incomingKey.equals(MainPageView.NAME)) {
        MainPageView contentView = new MainPageView(incomingContext);
        layoutView.setContentView(contentView);
      } else if (incomingKey.equals(SearchView.NAME)) {
        SearchView contentView = new SearchView(incomingContext);
        layoutView.setContentView(contentView);
      } else if (incomingKey.getClass().equals(AlbumListKey.class)) {
        ContentListView contentView = new ContentListView(incomingContext);
        layoutView.setContentView(contentView);
        contentView.setContent(((AlbumListKey) incomingKey).getAlbumList());
      } else if (incomingKey.getClass().equals(RadioListKey.class)) {
        ContentListView contentView = new ContentListView(incomingContext);
        layoutView.setContentView(contentView);
        contentView.setContent(((RadioListKey) incomingKey).getRadioList());
      } else if (incomingKey.getClass().equals(ContentKey.class)) {
        final Content content = ((ContentKey) incomingKey).getContent();
        if ("radio".equals(content.getType())) {
          RadioView contentView = new RadioView(incomingContext);
          layoutView.setContentView(contentView);
          contentView.setRadio(content);
          contentView.refresh();
        } else if ("music".equals(content.getType())) {
          AlbumView contentView = new AlbumView(incomingContext);
          layoutView.setContentView(contentView);
          contentView.setAlbum(content);
          contentView.refresh();
        } else {
          String message = "unknown content type: " + content.getType();
          Toast.makeText(incomingContext, message, Toast.LENGTH_LONG).show();
        }
      } else {
        final String message = String.format(
            "[%s]%s is under construct", incomingKey.getClass().getSimpleName(), incomingKey);
        Toast.makeText(incomingContext, message, Toast.LENGTH_LONG).show();
      }
      callback.onTraversalCompleted();
    }
  }

}
