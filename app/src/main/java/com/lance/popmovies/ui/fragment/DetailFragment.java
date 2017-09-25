package com.lance.popmovies.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lance.popmovies.R;
import com.lance.popmovies.db.Movie;
import com.lance.popmovies.network.okhttp.NetworkUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class DetailFragment extends LazyLoadFragment {
    public static final String ARG_MOVIE = "movie";

    private View mView;

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;
    private TextView mDetailErrorTextView;

    private Movie mMovie;

    //标志位，标志已经初始化完成
    private boolean isPrepared;
    //是否已被加载过一次，第二次就不再去请求数据了
    private boolean mHasLoadedOnce;

    public static DetailFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mMovie = getArguments().getParcelable(ARG_MOVIE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_detail, container, false);
            mTitleTextView = mView.findViewById(R.id.tv_detail_title);
            mPosterImageView = mView.findViewById(R.id.iv_detail_poster);
            mReleaseDateTextView = mView.findViewById(R.id.tv_detail_release_date);
            mVoteAverageTextView = mView.findViewById(R.id.tv_detail_vote_average);
            mOverviewTextView = mView.findViewById(R.id.tv_detail_overview);
            mDetailErrorTextView = mView.findViewById(R.id.tv_detail_error);
            isPrepared = true;
            lazyLoad();

        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }

    private void showErrorView() {
        mTitleTextView.setVisibility(View.INVISIBLE);
        mPosterImageView.setVisibility(View.INVISIBLE);
        mReleaseDateTextView.setVisibility(View.INVISIBLE);
        mVoteAverageTextView.setVisibility(View.INVISIBLE);
        mOverviewTextView.setVisibility(View.INVISIBLE);
        mDetailErrorTextView.setVisibility(View.VISIBLE);
    }

    private void initDetail() {
        mTitleTextView.setText(mMovie.getTitle());
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/" + mMovie.getPoster_path())
                .into(mPosterImageView);
        mReleaseDateTextView.setText(mMovie.getRelease_date());
        mVoteAverageTextView.setText(String.format("%2.1f", mMovie.getVote_average()));
        mOverviewTextView.setText(mMovie.getOverview());
    }
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        if (NetworkUtil.isNetworkAvailableAndConnected(getContext())) {
            initDetail();
            mHasLoadedOnce = true;
        } else {
            showErrorView();
        }
    }
}
