package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MainPage;
import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.api.entity.MoeFmMainPage;
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.CardClusterViewModelListAdapter;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity.MoeFmMainPageAdapter;

import java.lang.ref.WeakReference;
import java.util.Locale;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Main2Activity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);
    setSupportActionBar((Toolbar) findViewById(R.id.action_bar));
    final RecyclerView contentList = (RecyclerView) findViewById(R.id.content_list);
    MoeFmApplication.get(this).getAppComponent().getRadioService().mainPage()
        .subscribeOn(Schedulers.io())
        .first()
        .toSingle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleSubscriber<MainPage>() {

          final WeakReference<RecyclerView> contentViewWeakReference =
              new WeakReference<>(contentList);

          @Override
          public void onSuccess(MainPage value) {
            RecyclerView contentView = contentViewWeakReference.get();
            if (contentView != null) {
              if (value instanceof MoeFmMainPage) {
                contentView.setAdapter(new CardClusterViewModelListAdapter(
                    MoeFmMainPageAdapter.newCardClusterViewModelList((MoeFmMainPage) value), 4
                ));
              }
            }
          }

          @Override
          public void onError(Throwable error) {
            String message = String.format(
                Locale.US, "[%s]%s", error.getClass().getSimpleName(), error.getMessage());
            Toast.makeText(Main2Activity.this, message, Toast.LENGTH_LONG).show();
          }

        });
  }

}
