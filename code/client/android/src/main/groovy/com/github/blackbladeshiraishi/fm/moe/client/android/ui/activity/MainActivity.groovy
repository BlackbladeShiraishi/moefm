package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.ListHotRadiosViewHolder
import com.github.blackbladeshiraishi.fm.moe.facade.presenter.ListHotRadiosPresenter
import rx.android.schedulers.AndroidSchedulers

public class MainActivity extends AppCompatActivity {

  private ListHotRadiosViewHolder listHotRadiosViewHolder
  private ListHotRadiosPresenter listHotRadiosPresenter

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    title = getString R.string.activity_title_hot_radios

    contentView = R.layout.list_hot_radios
    listHotRadiosViewHolder = new ListHotRadiosViewHolder(findViewById(android.R.id.content))
    listHotRadiosPresenter = new ListHotRadiosPresenter(
        MoeFmApplication.get(this).appComponent.radioService,
        AndroidSchedulers.mainThread()
    )
    // on load error
    listHotRadiosPresenter.eventBus().ofType(ListHotRadiosPresenter.LoadErrorEvent)
        .observeOn(AndroidSchedulers.mainThread()).subscribe {
      ListHotRadiosPresenter.LoadErrorEvent event ->
        def e = event.error
        Toast.makeText(this, "[${e.class.simpleName}]${e.getMessage()}", Toast.LENGTH_LONG).show()
        Log.e("LOAD", "LoadErrorEvent", e)
    }
  }

  @Override
  protected void onStart() {
    super.onStart()
    listHotRadiosPresenter.start()
    listHotRadiosPresenter.bindView(listHotRadiosViewHolder)
  }

  @Override
  protected void onStop() {
    listHotRadiosPresenter.unbindView()
    super.onStop()
  }
}
