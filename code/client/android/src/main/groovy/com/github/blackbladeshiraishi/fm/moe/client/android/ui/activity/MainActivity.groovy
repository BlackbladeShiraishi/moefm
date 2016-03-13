package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.RadiosAdapter
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class MainActivity extends AppCompatActivity {

  final RadiosAdapter hotRadiosAdapter = new RadiosAdapter()

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    title = getString R.string.activity_title_hot_radios
    setContentView(R.layout.activity_main)
    (findViewById(R.id.hot_radios_list) as RecyclerView).with {
      adapter = hotRadiosAdapter
      layoutManager = new LinearLayoutManager(this)
    }

    MoeFmApplication.get(this).appComponent.radioService.hotRadios()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({Radio hotRadio ->
          hotRadiosAdapter.with {
            radios.add(hotRadio)
            notifyItemInserted(radios.size() - 1)
          }
        }, {Throwable e ->
          Toast.makeText(this, "[${e.class.simpleName}]${e.getMessage()}", Toast.LENGTH_LONG).show()
        })
  }

}
