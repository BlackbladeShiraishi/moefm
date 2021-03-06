package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.client.android.MoeFmApplication;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.service.MusicService;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SongListView extends FrameLayout {

  private final SongListAdapter songListAdapter;

  private final List<Song> songList = new ArrayList<>();

  public SongListView(Context context) {
    super(context);
  }

  public SongListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SongListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SongListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    final LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_song_list, this);
    final RecyclerView songListView = (RecyclerView) findViewById(R.id.song_list);

    songListAdapter = new SongListAdapter();
    songListView.setAdapter(songListAdapter);

    final FloatingActionButton playAllButton = (FloatingActionButton) findViewById(R.id.fab);
    playAllButton.setOnClickListener(this::onClickPlayAll);
  }

  public void setSongList(List<Song> songList) {
    this.songList.clear();
    this.songList.addAll(songList);
    songListAdapter.notifyDataSetChanged();
  }

  private void onClickPlayAll(@Nonnull View playAllButton) {
    final Context context = playAllButton.getContext();
    for (Song song : songList) {
      MusicService.playSong(context, song);
    }
  }

  private static String selectCover(Map<String, String> cover) {
    final String[] COVER_KEY = {"square", "small", "medium", "large"};
    String result = null;
    for (String key : COVER_KEY) {
      result = cover.get(key);
      if (result != null) {
        break;
      }
    }
    return result;
  }

  private class SongListAdapter extends RecyclerView.Adapter<SongItemViewHolder> {
    private final int IMAGE_VIEW_SUBSCRIPTION_TAG_KEY =
        R.id.view_song_list_item_cover_image_loading_request_tag_key;

    @Override
    public SongItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View rootView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_item_single_line_with_avatar, parent, false);
      return new SongItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final SongItemViewHolder holder, int position) {
      Song song = songList.get(position);
      holder.titleView.setText(song.getTitle());
      cancelLoadCover(holder.imageView);
      startLoadCover(song, holder.imageView);
      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          final int position = holder.getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            onClickSong(v, songList.get(position));
          }
        }
        private void onClickSong(View v, Song song) {
          MusicService.playSong(v.getContext(), song);
        }
      });
    }

    @Override
    public int getItemCount() {
      return songList.size();
    }

    private void startLoadCover(Song song, ImageView imageView) {
      Subscription subscribe = MoeFmApplication.get(imageView.getContext()).getAppComponent()
          .getRadioService()
          .albumDetail(song.getAlbumId())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.io())
          .subscribe(content -> {
            Picasso
                .with(imageView.getContext())
                .load(selectCover(content.getCover()))
                .into(imageView);
          }, error -> {
            // TODO
            String message = "can't load albumDetail to set cover of song: " + song.getTitle();
            new RuntimeException(message, error).printStackTrace();
          });
      imageView.setTag(IMAGE_VIEW_SUBSCRIPTION_TAG_KEY, subscribe);
    }
    private void cancelLoadCover(ImageView imageView) {
      Subscription oldLoading = (Subscription) imageView.getTag(IMAGE_VIEW_SUBSCRIPTION_TAG_KEY);
      if (oldLoading != null) {
        oldLoading.unsubscribe();
      }
      Picasso.with(imageView.getContext()).cancelRequest(imageView);
      imageView.setImageDrawable(null);
    }
  }

  private static class SongItemViewHolder extends RecyclerView.ViewHolder {
    final ImageView imageView;
    final TextView titleView;

    SongItemViewHolder(View itemView) {
      super(itemView);
      imageView = (ImageView) itemView.findViewById(R.id.image_view);
      titleView = (TextView) itemView.findViewById(R.id.text_view);;
    }
  }

}
