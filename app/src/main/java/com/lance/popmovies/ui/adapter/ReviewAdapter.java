package com.lance.popmovies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lance.popmovies.R;
import com.lance.popmovies.bean.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mContext;

    private List<Review> mReviews = new ArrayList<>();

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.detail_review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mReviewUserText.setText(review.getReviewAuthor());
        holder.mReviewContentText.setText(review.getReviewContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void refreshReviewList(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView mReviewUserText;
        public TextView mReviewContentText;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mReviewUserText = itemView.findViewById(R.id.tv_detail_review_user);
            mReviewContentText = itemView.findViewById(R.id.tv_detail_review_content);
        }
    }
}
