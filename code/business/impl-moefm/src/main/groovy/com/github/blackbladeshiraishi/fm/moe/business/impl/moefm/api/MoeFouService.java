package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.api;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * api of https://api.moefou.org/
 * @see <pre>http://open.moefou.org/docs/moe.fm</pre>
 */
public interface MoeFouService {

  /**
   * 电台的曲目
   */
  // radio/relationships.json?obj_type=song&wiki_id={radio_id}&api_key={api_key}
  @GET("radio/relationships.json?obj_type=song")
  Observable<ResponseBody> radioSongs(
      @Query("api_key") String apiKey, @Query("wiki_id") long radioId);

  /**
   * 专辑列表
   */
  // wikis?wiki_type=music&api_key={api_key}
  @GET("wikis?wiki_type=music")
  Observable<ResponseBody> albums(@Query("api_key") String apiKey);

  /**
   * 专辑的曲目
   */
  // music/subs.json?sub_type=song&wiki_id={album_id}&api_key={api_key}
  @GET("music/subs.json?sub_type=song")
  Observable<ResponseBody> albumSongs(
      @Query("api_key") String apiKey, @Query("wiki_id") long albumId);

}
