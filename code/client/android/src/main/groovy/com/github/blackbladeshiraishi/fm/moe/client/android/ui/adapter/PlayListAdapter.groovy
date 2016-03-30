package com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.business.api.PlayList
import com.github.blackbladeshiraishi.fm.moe.business.api.PlayService
import com.github.blackbladeshiraishi.fm.moe.client.android.R
import rx.Subscription
import rx.subscriptions.Subscriptions

import javax.annotation.Nonnull

/**
 * call {@link PlayListAdapter#release()} finally
 */
class PlayListAdapter extends RecyclerView.Adapter<SongsViewHolder> {

  @Nonnull final PlayList playList
  @Nonnull final PlayService playService
  @Nonnull private final Subscription subscription

  PlayListAdapter(@Nonnull PlayList playList, @Nonnull PlayService playService) {
    this.playList = playList
    this.playService = playService
    subscription = Subscriptions.from(
        playList.eventBus().ofType(PlayList.AddSongEvent).subscribe{PlayList.AddSongEvent event ->
          notifyItemInserted(event.location)
        },
        playList.eventBus().ofType(PlayList.RemoveSongEvent).subscribe{PlayList.RemoveSongEvent e->
          notifyItemRemoved(e.location)
        },
        playList.eventBus().ofType(PlayList.MoveSongEvent).subscribe{PlayList.MoveSongEvent event->
          notifyItemMoved(event.oldLocation, event.newLocation)
        },

        playService.eventBus().subscribe{PlayService.Event event ->
          if (event.location < playList.size()) {
            notifyItemChanged(event.location)
          }
          if (event instanceof PlayService.LocationChangeEvent) {
            if (event.oldLocation < playList.size()) {
              notifyItemChanged(event.oldLocation)
            }
          }
        }
    )
  }

  void release() {
    if (!subscription.unsubscribed) {
      subscription.unsubscribe()
    }
  }

  @Override
  protected void finalize() throws Throwable {
    release()
    super.finalize()
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
      String songTitle = playList.get(position).title
      if (position == playService.location) {
        def stateString = playService.state.toString()
        if (playService.state == PlayService.State.Playing) {
          stateString = title.context.getString(R.string.state_playing)
        } else if (playService.state == PlayService.State.Pausing) {
          stateString = title.context.getString(R.string.state_pausing)
        }
        songTitle = "[${stateString}]$songTitle"//TODO
      }
      title.text = songTitle
      title.onClickListener = {
        if (adapterPosition != RecyclerView.NO_POSITION) {
          playService.location = adapterPosition
        }
      }
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
