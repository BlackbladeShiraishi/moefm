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
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumListView extends FrameLayout {

  private final AlbumsAdapter albumListAdapter;

  private final List<Album> albums = new ArrayList<>();

  public AlbumListView(Context context) {
    super(context);
  }

  public AlbumListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AlbumListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public AlbumListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    final LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_album_list_content, this);
    final RecyclerView albumListView = (RecyclerView) findViewById(R.id.album_list);

    albumListAdapter = new AlbumsAdapter();
    albumListView.setAdapter(albumListAdapter);
  }

  public void setAlbums(List<Album> albums) {
    this.albums.clear();
    this.albums.addAll(albums);
    albumListAdapter.notifyDataSetChanged();
  }

  private class AlbumsAdapter extends RecyclerView.Adapter<AlbumItemViewHolder> {
    @Override
    public AlbumItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View rootView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_radio, parent, false);
      return new AlbumItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(AlbumItemViewHolder holder, int position) {
      final String title = albums.get(position).getTitle();
      holder.titleView.setText(title);
    }

    @Override
    public int getItemCount() {
      return albums.size();
    }
  }

  private static class AlbumItemViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;

    AlbumItemViewHolder(View itemView) {
      super(itemView);
      titleView = (TextView) itemView;
    }
  }

}
