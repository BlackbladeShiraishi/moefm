package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

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
    Picasso.with(thumbView.getContext()).load(vm.getThumbPath()).into(thumbView);
    rootView.setOnClickListener(vm.getOnClickCardViewListener());
  }

  public interface CardViewModel {
    @Nullable
    String getTitle();
    @Nullable
    String getThumbPath();
    @Nullable
    View.OnClickListener getOnClickCardViewListener();
  }

}
