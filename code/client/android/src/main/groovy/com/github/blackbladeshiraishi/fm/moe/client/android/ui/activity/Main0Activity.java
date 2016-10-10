package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget.MainLayoutView;

import java.util.Map;

import flow.Direction;
import flow.Flow;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.State;
import flow.TraversalCallback;


public class Main0Activity extends AppCompatActivity {

  // ##### flow #####
  @Override
  protected void attachBaseContext(Context newBase) {
    newBase = Flow.configure(newBase, this)
        .dispatcher(KeyDispatcher.configure(this, new KeyChanger() {
          @Override
          public void changeKey(@Nullable State outgoingState, @NonNull State incomingState,
                                @NonNull Direction direction,
                                @NonNull Map<Object, Context> incomingContexts,
                                @NonNull TraversalCallback callback) {
            //TODO;
            callback.onTraversalCompleted();
          }
        }).build())
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
    setContentView(new MainLayoutView(this));
  }

}
