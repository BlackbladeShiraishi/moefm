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
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.AlbumKey;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.RadioKey;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import java.util.ArrayList;
import java.util.List;

import flow.Flow;

public class ContentListView extends FrameLayout {

  private final ContentListAdapter contentListAdapter;

  private final List<Content> content = new ArrayList<>();

  public ContentListView(Context context) {
    super(context);
  }

  public ContentListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ContentListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ContentListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    final LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_album_list_content, this);//TODO
    final RecyclerView contentListView = (RecyclerView) findViewById(R.id.album_list);

    contentListAdapter = new ContentListAdapter();
    contentListView.setAdapter(contentListAdapter);
  }

  public void setContent(List<Content> content) {
    this.content.clear();
    this.content.addAll(content);
    contentListAdapter.notifyDataSetChanged();
  }

  private class ContentListAdapter extends RecyclerView.Adapter<ContentItemViewHolder> {
    @Override
    public ContentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View rootView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_radio, parent, false);
      return new ContentItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ContentItemViewHolder holder, int position) {
      final String title = content.get(position).getTitle();
      holder.titleView.setText(title);
      holder.titleView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          final int position = holder.getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            onClickContent(v, content.get(position));
          }
        }
        private void onClickContent(View v, Content content) {
          if ("radio".equals(content.getType())) {
            final Radio radio = new Radio();
            radio.setId(content.getId());
            radio.setTitle(content.getTitle());
            radio.setModifiedUserId(content.getModifiedUserId());
            radio.setMeta(content.getMeta());
            radio.setCover(content.getCover());
            Flow.get(v).set(new RadioKey(radio));
          } else if ("music".equals(content.getType())) {
            final Album album = new Album();
            album.setId(content.getId());
            album.setTitle(content.getTitle());
            album.setMeta(content.getMeta());
            album.setCover(content.getCover());
            Flow.get(v).set(new AlbumKey(album));
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return content.size();
    }
  }

  private static class ContentItemViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;

    ContentItemViewHolder(View itemView) {
      super(itemView);
      titleView = (TextView) itemView;
    }
  }

}
