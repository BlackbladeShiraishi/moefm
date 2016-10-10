package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import flow.Flow;


public class Main0Activity extends AppCompatActivity {

  @Override
  protected void attachBaseContext(Context newBase) {
    newBase = Flow.configure(newBase, this).install();
    super.attachBaseContext(newBase);
  }

  @Override
  public void onBackPressed() {
    if (!Flow.get(this).goBack()) {
      super.onBackPressed();
    }
  }

}
