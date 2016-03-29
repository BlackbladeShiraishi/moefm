package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.CardClusterViewModelListAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity.MainPageAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity.Mocks;

public class Main2Activity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);
    RecyclerView contentList = (RecyclerView) findViewById(R.id.content_list);
    if (contentList != null) {
      contentList.setAdapter(new CardClusterViewModelListAdapter(
          MainPageAdapter.newCardClusterViewModelList(Mocks.mainPage(3, 2, 1, 4))));
    }
  }

}
