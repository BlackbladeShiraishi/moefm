package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.MoeFms
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class MainActivity extends AppCompatActivity {

    TextView mainTextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainTextView = findViewById(R.id.main_text) as TextView
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
                .create({ Observer<List<Radio>> subscriber ->
                    try {
                        subscriber.onNext(getHotRadios(apiKey))
                        subscriber.onCompleted()
                    } catch (Throwable e) {
                        subscriber.onError(e)
                    }
                } as Observable.OnSubscribe<List<Radio>>)
                .flatMap { Observable.from it }
    }

    private void test(String apiKey) {
        getHostRadiosObservable(apiKey)
                .subscribeOn(Schedulers.io())
                .map { it.with { "\nid: $id, title:$title" as String } }
                .concatWith(Observable.just('\n--- radio over ---'))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mainTextView.append it }, { mainTextView.append "\n--- $it ---" })
    }

}
