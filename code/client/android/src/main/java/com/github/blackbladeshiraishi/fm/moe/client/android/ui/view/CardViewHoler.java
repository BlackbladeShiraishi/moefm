package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;


public class CardViewHoler {

  public final View rootView;
  public final ImageView thumbView;
  public final TextView titleView;


  public CardViewHoler(View rootView) {
    this.rootView = rootView;
    this.thumbView = (ImageView) rootView.findViewById(R.id.thumb);
    this.titleView = (TextView) rootView.findViewById(R.id.title);
  }

  public void bindData(CardViewModel vm) {
    titleView.setText(vm.getTitle());
    thumbView.setImageResource(vm.getThumb());
  }

  public interface CardViewModel {
    String getTitle();
    int getThumb();
  }

}
