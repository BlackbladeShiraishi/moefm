package com.github.blackbladeshiraishi.fm.moe.client.android.ui.entity;

import android.view.View;

import com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.CardViewHoler;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class RadioListAdapter extends BaseCardClusterViewModel {

  public final List<Radio> radios;

  public RadioListAdapter(
      @Nullable View.OnClickListener onClickTitleContainerListener,
      @Nullable String title,
      @Nullable List<Radio> radios) {
    super(onClickTitleContainerListener, title);
    this.radios = radios;
  }


  @Nullable
  @Override
  public List<CardViewHoler.CardViewModel> getCardViewModels() {
    if (radios == null) {
      return null;
    }
    List<CardViewHoler.CardViewModel> result = new ArrayList<>(radios.size());
    for (Radio radio : radios) {
      result.add(new RadioAdapter(radio));
    }
    return result;
  }

}
