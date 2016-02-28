package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.service.ControllerService
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter.RadiosAdapter
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import com.github.blackbladeshiraishi.fm.moe.facade.controller.Controller
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

public class MainActivity extends AppCompatActivity {

  final RadiosAdapter hotRadiosAdapter = new RadiosAdapter()

  private Controller controller

  private ControllerServiceConnection connection = new ControllerServiceConnection()

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    (findViewById(R.id.hot_radios_list) as RecyclerView).with {
      adapter = hotRadiosAdapter
      layoutManager = new LinearLayoutManager(this)
    }
  }

  @Override
  protected void onStart() {
    super.onStart()

    def intent = new Intent(this, ControllerService)
    bindService(intent, connection, BIND_AUTO_CREATE)
  }

  @Override
  protected void onStop() {
    if (connection.bound) {
      unbindService(connection)
      controller = null
    }
    super.onStop()
  }


  private class ControllerServiceConnection implements ServiceConnection {

    boolean bound = false

    @Override
    void onServiceConnected(ComponentName name, IBinder service) {
      def binder = service as ControllerService.LocalBinder
      controller = binder.service.controller
      bound = true

      controller.hotRadiosExtension.hotRadios()
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

    @Override
    void onServiceDisconnected(ComponentName name) {
      bound = false
      controller = null
    }
  }

}
