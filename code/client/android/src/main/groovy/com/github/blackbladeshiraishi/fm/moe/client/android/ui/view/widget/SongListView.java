package com.github.blackbladeshiraishi.fm.moe.client.android.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.service.MusicService;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song;

import java.util.ArrayList;
import java.util.List;

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
    inflater.inflate(R.layout.view_album_list_content, this);//TODO
    final RecyclerView songListView = (RecyclerView) findViewById(R.id.album_list);

    songListAdapter = new SongListAdapter();
    songListView.setAdapter(songListAdapter);
  }

  public void setSongList(List<Song> songList) {
    this.songList.clear();
    this.songList.addAll(songList);
    songListAdapter.notifyDataSetChanged();
  }

  private class SongListAdapter extends RecyclerView.Adapter<SongItemViewHolder> {
    @Override
    public SongItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View rootView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_radio, parent, false);
      return new SongItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final SongItemViewHolder holder, int position) {
      final String title = songList.get(position).getTitle();
      holder.titleView.setText(title);
      holder.titleView.setOnClickListener(new OnClickListener() {
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
  }

  private static class SongItemViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;

    SongItemViewHolder(View itemView) {
      super(itemView);
      titleView = (TextView) itemView;
    }
  }

}
