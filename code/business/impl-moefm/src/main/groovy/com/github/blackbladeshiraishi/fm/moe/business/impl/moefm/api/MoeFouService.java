package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.api;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * api of https://api.moefou.org/
 */
public interface MoeFouService {

  // radio/relationships.json?obj_type=song&wiki_id={radio_id}&api_key={api_key}
  @GET("radio/relationships.json?obj_type=song")
  Observable<ResponseBody> radioSongs(
      @Query("api_key") String apiKey, @Query("wiki_id") long radioId);

}
