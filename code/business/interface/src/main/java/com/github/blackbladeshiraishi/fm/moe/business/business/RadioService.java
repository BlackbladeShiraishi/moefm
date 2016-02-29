package com.github.blackbladeshiraishi.fm.moe.business.business;

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import rx.Observable;

public interface RadioService {

  Observable<Radio> hotRadios();

}
