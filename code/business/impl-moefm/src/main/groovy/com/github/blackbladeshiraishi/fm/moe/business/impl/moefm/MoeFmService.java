package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MoeFmService {
  // explore?api=json&hot_radios=1&api_key={api_key}
  @GET("explore?api=json&hot_radios=1")
  Observable<ResponseBody> hotRadios(@Query("api_key") String apiKey);
}
