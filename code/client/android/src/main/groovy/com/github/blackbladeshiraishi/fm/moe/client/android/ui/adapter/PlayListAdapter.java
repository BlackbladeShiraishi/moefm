package com.github.blackbladeshiraishi.fm.moe.client.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.business.api.PlayList;
import com.github.blackbladeshiraishi.fm.moe.business.api.PlayService;
import com.github.blackbladeshiraishi.fm.moe.client.android.R;

import javax.annotation.Nonnull;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * call {@link PlayListAdapter#release()} finally
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.SongsViewHolder> {

  @Nonnull private final PlayList playList;
  @Nonnull private final PlayService playService;
  @Nonnull private final Subscription subscription;

  public PlayListAdapter(@Nonnull PlayList playList, @Nonnull PlayService playService) {
    this.playList = playList;
    this.playService = playService;
    subscription = Subscriptions.from(
        playList.eventBus().ofType(PlayList.AddSongEvent.class).subscribe( event ->
          notifyItemInserted(event.location)
        ),
        playList.eventBus().ofType(PlayList.RemoveSongEvent.class).subscribe( e->
          notifyItemRemoved(e.location)
        ),
        playList.eventBus().ofType(PlayList.MoveSongEvent.class).subscribe( event->
          notifyItemMoved(event.oldLocation, event.newLocation)
        ),

        playService.eventBus().subscribe( event -> {
          if (event.location < playList.size()) {
            notifyItemChanged(event.location);
          }
          if (event instanceof PlayService.LocationChangeEvent) {
            if (((PlayService.LocationChangeEvent)event).oldLocation < playList.size()) {
              notifyItemChanged(((PlayService.LocationChangeEvent) event).oldLocation);
            }
          }
        })
    );
  }

  public void release() {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  @Override
  protected void finalize() throws Throwable {
    release();
    super.finalize();
  }

  @Override
  public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_song, parent, false);
    return new SongsViewHolder(rootView);
  }

  @Override
  public void onBindViewHolder(SongsViewHolder holder, int position) {
    String songTitle = playList.get(position).getTitle();
    if (position == playService.getLocation()) {
      String stateString = playService.getState().toString();
      if (playService.getState() == PlayService.State.Playing) {
        stateString = holder.title.getContext().getString(R.string.state_playing);
      } else if (playService.getState() == PlayService.State.Pausing) {
        stateString = holder.title.getContext().getString(R.string.state_pausing);
      }
      songTitle = String.format("[%s]%s", stateString, songTitle);//TODO
    }
    holder.title.setText(songTitle);
    holder.title.setOnClickListener(view -> {
    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
      playService.setLocation(holder.getAdapterPosition());
    }
    });
  }

  @Override
  public int getItemCount() {
    return playList.size();
  }

  static class SongsViewHolder extends RecyclerView.ViewHolder {

    TextView title;

    SongsViewHolder(View itemView) {
      super(itemView);
      title = (TextView) itemView;
    }
  }
}
