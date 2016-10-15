package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.api;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
   * 搜索条目
   * @see <pre>http://open.moefou.org/docs/api/search/wiki</pre>
   */
  // search/wiki.json?keyword={keyword}&wiki_type={type}&api_key={api_key}
  @GET("search/wiki.json")
  Observable<ResponseBody> searchContents(
      @Nonnull @Query("api_key") String apiKey,
      @Nonnull @Query("keyword") String keyword,
      @Nullable @Query("wiki_type") String type);

  /**
   * 电台列表
   */
  // wikis?wiki_type=radio&api_key={api_key}
  @GET("wikis?wiki_type=radio")
  Observable<ResponseBody> radios(@Query("api_key") String apiKey);

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

  /**
   * 用户基本资料
   */
  // user/detail.json?uid={uid}&api_key={api_key}
  @GET("user/detail.json")
  Observable<ResponseBody> user(@Query("api_key") String apiKey, @Query("uid") String uid);

}
