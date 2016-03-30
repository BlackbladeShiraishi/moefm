package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * api of http://moe.fm/
 */
public interface MoeFmService {

  // explore?api=json&new_musics=1&hot_musics=1&hot_radios=1&musics=1&api_key={api_key}
  @GET("explore?api=json&new_musics=1&hot_musics=1&hot_radios=1&musics=1")
  Observable<ResponseBody> mainPage(@Query("api_key") String apiKey);

  // explore?api=json&hot_radios=1&api_key={api_key}
  @GET("explore?api=json&hot_radios=1")
  Observable<ResponseBody> hotRadios(@Query("api_key") String apiKey);

}
