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
import com.github.blackbladeshiraishi.fm.moe.client.android.ui.navigation.RadioKey;
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio;

import java.util.ArrayList;
import java.util.List;

import flow.Flow;

public class RadioListView extends FrameLayout {

  private final AlbumsAdapter albumListAdapter;

  private final List<Radio> radios = new ArrayList<>();

  public RadioListView(Context context) {
    super(context);
  }

  public RadioListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RadioListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public RadioListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  // init
  {
    final LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.view_album_list_content, this);//TODO
    final RecyclerView albumListView = (RecyclerView) findViewById(R.id.album_list);

    albumListAdapter = new AlbumsAdapter();
    albumListView.setAdapter(albumListAdapter);
  }

  public void setRadios(List<Radio> radios) {
    this.radios.clear();
    this.radios.addAll(radios);
    albumListAdapter.notifyDataSetChanged();
  }

  private class AlbumsAdapter extends RecyclerView.Adapter<RadioItemViewHolder> {
    @Override
    public RadioItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View rootView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_radio, parent, false);
      return new RadioItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RadioItemViewHolder holder, int position) {
      final String title = radios.get(position).getTitle();
      holder.titleView.setText(title);
      holder.titleView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          final int position = holder.getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            Flow.get(v).set(new RadioKey(radios.get(position)));
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return radios.size();
    }
  }

  private static class RadioItemViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;

    RadioItemViewHolder(View itemView) {
      super(itemView);
      titleView = (TextView) itemView;
    }
  }

}
