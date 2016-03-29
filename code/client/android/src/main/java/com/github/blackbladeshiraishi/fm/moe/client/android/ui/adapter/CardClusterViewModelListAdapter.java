package com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardClusterViewHolder;

import java.util.List;

import javax.annotation.Nonnull;


public class CardClusterViewModelListAdapter extends RecyclerView.Adapter<CardClusterViewHolder> {

  @Nonnull
  public final List<CardClusterViewHolder.CardClusterViewModel> dataSet;

  public CardClusterViewModelListAdapter(
      @Nonnull List<CardClusterViewHolder.CardClusterViewModel> dataSet) {
    this.dataSet = dataSet;
  }

  @Override
  public CardClusterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_cluster, parent, false);
    return new CardClusterViewHolder(view);
  }

  @Override
  public void onBindViewHolder(CardClusterViewHolder holder, int position) {
    holder.bindData(dataSet.get(position));
  }

  @Override
  public int getItemCount() {
    return dataSet.size();
  }

}
