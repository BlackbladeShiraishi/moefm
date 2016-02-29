package com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity.RadioActivity
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio

class RadiosAdapter extends RecyclerView.Adapter<RadiosViewHolder> {

  List<Radio> radios = []

  @Override
  RadiosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    def rootView = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_radio, parent, false)
    return new RadiosViewHolder(rootView)
  }

  @Override
  void onBindViewHolder(RadiosViewHolder holder, int position) {
    holder.with {
      title.text = radios[position].title
      title.setOnClickListener{RadioActivity.startThis(title.context, radios[position])}//TODO posi
    }
  }

  @Override
  int getItemCount() {
    return radios.size()
  }

  @Override
  long getItemId(int position) {
    return radios[position].id
  }

  static class RadiosViewHolder extends RecyclerView.ViewHolder {

    TextView title

    RadiosViewHolder(View itemView) {
      super(itemView)
      title = itemView as TextView
    }
  }

}
