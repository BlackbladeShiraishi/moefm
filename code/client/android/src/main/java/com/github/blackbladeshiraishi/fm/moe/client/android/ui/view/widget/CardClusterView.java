package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;

import java.util.List;

import javax.annotation.Nullable;

public class CardClusterView extends FrameLayout {

  public final ViewGroup titleContainer;
  public final TextView title;
  public final View moreButton;
  public final ViewGroup cardContainer;

  public CardClusterView(Context context) {
    super(context);
  }

  public CardClusterView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CardClusterView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CardClusterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    final LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_card_cluster, this);
    titleContainer = (ViewGroup) findViewById(R.id.title_container);
    title = (TextView) titleContainer.findViewById(R.id.title);
    moreButton = titleContainer.findViewById(R.id.more_button);
    cardContainer = (ViewGroup) findViewById(R.id.card_container);
  }

  public void bindData(CardClusterViewModel vm, int maxColumn) {
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
      int counter = 0; // have added counter cards
      for (CardViewHoler.CardViewModel item: cardViewModels) {
        if (counter >= maxColumn) {
          break;
        }
        CardViewHoler cardViewHoler =
            new CardViewHoler(inflater.inflate(R.layout.view_card, cardContainer, false));
        cardViewHoler.bindData(item);
        cardContainer.addView(cardViewHoler.rootView);
        counter++;
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
