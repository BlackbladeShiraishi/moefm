package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.MoeFms
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class MainActivity extends AppCompatActivity {

  HotRadiosAdapter hotRadiosAdapter

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    hotRadiosAdapter = new HotRadiosAdapter()
    (findViewById(R.id.hot_radios_list) as RecyclerView).with {
      adapter = hotRadiosAdapter
      layoutManager = new LinearLayoutManager(this)
    }
  }

  @Override
  protected void onStart() {
    super.onStart()
    test(getString(R.string.moefm_api_key))
  }

  private static List<Radio> getHotRadios(String apiKey) {
    MoeFms.listHotRadios(MoeFms.newRetrofit(), apiKey)
  }

  private static Observable<Radio> getHostRadiosObservable(String apiKey) {
    Observable
        .create({Observer<List<Radio>> subscriber ->
          try {
            subscriber.onNext(getHotRadios(apiKey))
            subscriber.onCompleted()
          } catch (Throwable e) {
            subscriber.onError(e)
          }
        } as Observable.OnSubscribe<List<Radio>>)
        .flatMap {Observable.from it}
  }

  private void test(String apiKey) {
    getHostRadiosObservable(apiKey)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {Radio radio ->
          hotRadiosAdapter.with {
            radios << radio
            notifyItemInserted(radios.size() - 1)
          }
        }
  }

  private static class HotRadiosViewHolder extends RecyclerView.ViewHolder {

    TextView title

    HotRadiosViewHolder(View itemView) {
      super(itemView)
      title = itemView as TextView
    }
  }

  private static class HotRadiosAdapter extends RecyclerView.Adapter<HotRadiosViewHolder> {

    List<Radio> radios = []

    @Override
    HotRadiosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      def rootView = LayoutInflater.from(parent.context)
          .inflate(R.layout.list_item_hot_radio, parent, false)
      return new HotRadiosViewHolder(rootView)
    }

    @Override
    void onBindViewHolder(HotRadiosViewHolder holder, int position) {
      holder.title.text = radios[position].title
    }

    @Override
    int getItemCount() {
      return radios.size()
    }

    @Override
    long getItemId(int position) {
      return radios[position].id
    }
  }
}
