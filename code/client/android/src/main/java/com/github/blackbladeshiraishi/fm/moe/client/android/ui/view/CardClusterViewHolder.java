package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;

import java.util.List;

import javax.annotation.Nullable;



public class CardClusterViewHolder extends RecyclerView.ViewHolder {

  public final ViewGroup titleContainer;
  public final TextView title;
  public final View moreButton;
  public final ViewGroup cardContainer;

  public CardClusterViewHolder(View itemView) {
    super(itemView);
    titleContainer = (ViewGroup) itemView.findViewById(R.id.title_container);
    title = (TextView) titleContainer.findViewById(R.id.title);
    moreButton = titleContainer.findViewById(R.id.more_button);
    cardContainer = (ViewGroup) itemView.findViewById(R.id.card_container);
  }

  public void bindData(CardClusterViewModel vm) {
    // set more button
    View.OnClickListener titleClickListener = vm.getOnClickTitleContainerListener();
    if (titleClickListener != null) {
      titleContainer.setOnClickListener(titleClickListener);
      moreButton.setVisibility(View.VISIBLE);
    } else {
      titleContainer.setOnClickListener(null);
      moreButton.setVisibility(View.GONE);
    }
    // set title
    String titleString = vm.getTitle();
    if (titleString != null) {
      title.setText(titleString);
    } else {
      title.setText("");
    }
    // set cardContainer
    List<CardViewHoler.CardViewModel> cardViewModels = vm.getCardViewModels();
    cardContainer.removeAllViews();
    if (cardViewModels != null) {
      cardContainer.setVisibility(View.VISIBLE);
      LayoutInflater inflater = LayoutInflater.from(cardContainer.getContext());
      for (CardViewHoler.CardViewModel item: cardViewModels) {
        CardViewHoler cardViewHoler =
            new CardViewHoler(inflater.inflate(R.layout.view_card, cardContainer, false));
        cardViewHoler.bindData(item);
        cardContainer.addView(cardViewHoler.rootView);
      }
    } else {
      cardContainer.setVisibility(View.GONE);
    }
  }

  public interface CardClusterViewModel {
    @Nullable
    View.OnClickListener getOnClickTitleContainerListener();
    @Nullable
    String getTitle();
    @Nullable
    List<CardViewHoler.CardViewModel> getCardViewModels();
  }

}
