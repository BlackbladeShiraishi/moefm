package com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.business.business.PlayList
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import rx.Subscription
import rx.subscriptions.Subscriptions

import javax.annotation.Nonnull

/**
 * call {@link PlayListAdapter#release()} finally
 */
class PlayListAdapter extends RecyclerView.Adapter<SongsViewHolder> {

  @Nonnull final PlayList playList
  @Nonnull final Subscription subscription

  PlayListAdapter(@Nonnull PlayList playList) {
    this.playList = playList
    subscription = Subscriptions.from(
        playList.eventBus().ofType(PlayList.AddSongEvent).subscribe{PlayList.AddSongEvent event ->
          notifyItemInserted(event.location)
        },
        playList.eventBus().ofType(PlayList.RemoveSongEvent).subscribe{PlayList.RemoveSongEvent e->
          notifyItemRemoved(e.location)
        },
        playList.eventBus().ofType(PlayList.MoveSongEvent).subscribe{PlayList.MoveSongEvent event->
          notifyItemMoved(event.oldLocation, event.newLocation)
        }
    )
  }

  void release() {
    subscription.unsubscribe()
  }

  @Override
  SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    def rootView = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_song, parent, false)
    return new SongsViewHolder(rootView)
  }

  @Override
  void onBindViewHolder(SongsViewHolder holder, int position) {
    holder.with {
      title.text = playList.get(position).title
    }
  }

  @Override
  int getItemCount() {
    return playList.size()
  }

  static class SongsViewHolder extends RecyclerView.ViewHolder {

    TextView title

    SongsViewHolder(View itemView) {
      super(itemView)
      title = itemView as TextView
    }
  }
}
