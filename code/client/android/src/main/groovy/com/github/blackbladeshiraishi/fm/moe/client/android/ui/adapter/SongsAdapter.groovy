package com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import com.github.blackbladeshiraishi.fm.moe.client.android.service.MusicService
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song

class SongsAdapter extends RecyclerView.Adapter<SongsViewHolder> {

  List<Song> songs = []

  @Override
  SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    def rootView = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_song, parent, false)
    return new SongsViewHolder(rootView)
  }

  @Override
  void onBindViewHolder(SongsViewHolder holder, int position) {
    holder.with {
      title.text = songs[position].title
      //TODO duplication?
      title.onClickListener = {
        if (adapterPosition != RecyclerView.NO_POSITION) {
          MusicService.playSong(title.context, songs[adapterPosition])
        }
      }
    }
  }

  @Override
  int getItemCount() {
    return songs.size()
  }

  @Override
  long getItemId(int position) {
    return songs[position].id
  }

  static class SongsViewHolder extends RecyclerView.ViewHolder {

    TextView title

    SongsViewHolder(View itemView) {
      super(itemView)
      title = itemView as TextView
    }
  }

}
