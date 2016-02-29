package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.RadiosAdapter
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

public class MainActivity extends AppCompatActivity {

  final RadiosAdapter hotRadiosAdapter = new RadiosAdapter()

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    (findViewById(R.id.hot_radios_list) as RecyclerView).with {
      adapter = hotRadiosAdapter
      layoutManager = new LinearLayoutManager(this)
    }

    final def listHotRadios = MoeFmApplication.get(this).appComponent.listHotRadios
    Observable
        .create({Subscriber<List<Radio>> subscriber ->
          try {
            def result = listHotRadios.execute()
            if (!subscriber.unsubscribed) {
              subscriber.onNext(result)
              subscriber.onCompleted()
            }
          } catch (Throwable e) {
            subscriber.onError(e)
          }
        } as Observable.OnSubscribe<List<Radio>>)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<Radio>>() {
          @Override
          void call(List<Radio> hotRadios) {
            hotRadiosAdapter.with {
              radios.clear()
              radios.addAll(hotRadios)
              notifyDataSetChanged()
            }
          }
        })
  }

}
