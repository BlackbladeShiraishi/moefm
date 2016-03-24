package com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity.RadioActivity
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import gq.baijie.rxlist.ObservableList
import rx.Observable
import rx.Subscription
import rx.subscriptions.Subscriptions

import javax.annotation.Nonnull
import javax.annotation.Nullable

class HotRadiosAdapter extends RecyclerView.Adapter<RadiosViewHolder> {

  private static final ObservableList<Radio> EMPTY = new ObservableList<>(Collections.EMPTY_LIST)

  @Nonnull
  ObservableList<Radio> radios = EMPTY

  @Nullable
  Subscription radioListSubscription

  @Override
  protected void finalize() throws Throwable {
    unbindRadios()
    super.finalize()
  }

  void bindRadios(@Nonnull ObservableList<Radio> radios) {
    if (this.radios.is(radios)) {
      return
    }
    unbindRadios()
    this.radios = radios
    notifyDataSetChanged()
    radioListSubscription = Subscriptions.from(
        events(radios, ObservableList.AddEvent).subscribe {
          notifyItemInserted(it.index)
        },
        events(radios, ObservableList.RemoveEvent).subscribe {
          notifyItemRemoved(it.index)
        },
        events(radios, ObservableList.SetEvent).subscribe {
          notifyItemChanged(it.index)
        }
    )
  }

  void unbindRadios() {
    if (radioListSubscription != null) {
      if (!radioListSubscription.unsubscribed) {
        radioListSubscription.unsubscribe()
      }
      radioListSubscription = null
    }
    if (!EMPTY.is(radios)) {
      radios = EMPTY
      notifyDataSetChanged()
    }
  }

  private static <EventType> Observable<EventType> events(
      ObservableList<Radio> hotRadios, Class<EventType> type) {
    // shouldn't use observeOn(AndroidSchedulers.mainThread()) or observeOn(Schedulers.trampoline())
    // 1. hotRadios must be modified in Main Thread
    // 2. must notify adapter immediately
    // see https://code.google.com/p/android/issues/detail?id=77846#c32
    return hotRadios.eventBus().ofType(type)
  }

  @Override
  RadiosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    def rootView = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_radio, parent, false)
    return new RadiosViewHolder(rootView)
  }

  @Override
  void onBindViewHolder(RadiosViewHolder holder, int position) {
    holder.with {
      title.text = radios.list()[position].title
      title.setOnClickListener {
        if (adapterPosition != RecyclerView.NO_POSITION) {
          RadioActivity.startThis(title.context, radios.list()[adapterPosition])
        }
      }
    }
  }

  @Override
  int getItemCount() {
    return radios.list().size()
  }

  @Override
  long getItemId(int position) {
    return radios.list()[position].id
  }

  static class RadiosViewHolder extends RecyclerView.ViewHolder {

    TextView title

    RadiosViewHolder(View itemView) {
      super(itemView)
      title = itemView as TextView
    }
  }

}
