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
import android.widget.ImageView;
import android.widget.TextView;

import com.github.blackbladeshiraishi.fm.moe.client.android.R;
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.ContentKey;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Content;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  private class ContentListAdapter extends RecyclerView.Adapter<ContentItemViewHolder> {
    @Override
    public ContentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View rootView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_item_single_line_with_avatar, parent, false);
      return new ContentItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ContentItemViewHolder holder, int position) {
      final Content currentContent = ContentListView.this.content.get(position);
      Picasso.with(holder.imageView.getContext())
          .load(selectCover(currentContent.getCover()))
          .into(holder.imageView);
      holder.titleView.setText(currentContent.getTitle());
      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          final int position = holder.getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            onClickContent(v, content.get(position));
          }
        }
        private void onClickContent(View v, Content content) {
          Flow.get(v).set(new ContentKey(content));
        }
      });
    }

    @Override
    public int getItemCount() {
      return content.size();
    }

  }

  private static class ContentItemViewHolder extends RecyclerView.ViewHolder {
    final ImageView imageView;
    final TextView titleView;

    ContentItemViewHolder(View itemView) {
      super(itemView);
      imageView = (ImageView) itemView.findViewById(R.id.image_view);
      titleView = (TextView) itemView.findViewById(R.id.text_view);
    }
  }

}
