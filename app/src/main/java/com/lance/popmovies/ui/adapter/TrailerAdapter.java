package com.lance.popmovies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lance.popmovies.R;
import com.lance.popmovies.bean.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private Context mContext;

    private List<Trailer> mTrailers = new ArrayList<>();

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public TrailerAdapter(Context context, ListItemClickListener onClickListener) {
        mContext = context;
        mOnClickListener = onClickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.detail_trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        holder.mTrailerText.setText(trailer.getVideoName());
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void refreshTrailerList(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    //预告片ViewHolder
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTrailerText;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerText = itemView.findViewById(R.id.tv_detail_trailer_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
