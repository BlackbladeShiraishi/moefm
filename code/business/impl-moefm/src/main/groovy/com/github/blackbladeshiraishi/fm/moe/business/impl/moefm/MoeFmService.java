package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MoeFmService {
  // use moe.fm api
  // explore?api=json&hot_radios=1&api_key={api_key}
  @GET("explore?api=json&hot_radios=1")
  Observable<ResponseBody> hotRadios(@Query("api_key") String apiKey);

  // use moefou.org api
  // radio/relationships.json?obj_type=song&wiki_id={radio_id}&api_key={api_key}
  @GET("radio/relationships.json?obj_type=song")
  Observable<ResponseBody> radioSongs(
      @Query("api_key") String apiKey, @Query("wiki_id") long radioId);

}
