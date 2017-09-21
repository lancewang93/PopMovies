package com.lance.popmovies.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class DetailFragment extends Fragment {
    public static final String ARG_MOVIE = "movie";

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;

    private TextView mTitleTextView;
    private ImageView mBackdropImageView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;
    private TextView mDetailErrorTextView;

    private Movie mMovie;

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
        setHasOptionsMenu(true);
        mMovie = getArguments().getParcelable(ARG_MOVIE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mCollapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        mToolbar = view.findViewById(R.id.toolbar);
        mTitleTextView = view.findViewById(R.id.tv_detail_title);
        mBackdropImageView = view.findViewById(R.id.iv_detail_backdrop);
        mPosterImageView = view.findViewById(R.id.iv_detail_poster);
        mReleaseDateTextView = view.findViewById(R.id.tv_detail_release_date);
        mVoteAverageTextView = view.findViewById(R.id.tv_detail_vote_average);
        mOverviewTextView = view.findViewById(R.id.tv_detail_overview);
        mDetailErrorTextView = view.findViewById(R.id.tv_detail_error);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (NetworkUtil.isNetworkAvailableAndConnected(getContext())) {
            initCollapsingToolBar();
            initDetail();
        } else {
            showErrorView();
        }
    }

    private void showErrorView() {
        mTitleTextView.setVisibility(View.INVISIBLE);
        mBackdropImageView.setVisibility(View.INVISIBLE);
        mPosterImageView.setVisibility(View.INVISIBLE);
        mReleaseDateTextView.setVisibility(View.INVISIBLE);
        mVoteAverageTextView.setVisibility(View.INVISIBLE);
        mOverviewTextView.setVisibility(View.INVISIBLE);
        mDetailErrorTextView.setVisibility(View.VISIBLE);
    }

    private void initCollapsingToolBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitleEnabled(true);
    }

    private void initDetail() {
        mTitleTextView.setText(mMovie.getTitle());
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w780/" + mMovie.getBackdrop_path())
                .into(mBackdropImageView);
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/" + mMovie.getPoster_path())
                .into(mPosterImageView);
        mReleaseDateTextView.setText(mMovie.getRelease_date());
        mVoteAverageTextView.setText(String.format("%2.1f", mMovie.getVote_average()));
        mOverviewTextView.setText(mMovie.getOverview());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        String mineType = "text/plain";
        String title = getString(R.string.share_title);
        ShareCompat.IntentBuilder
                .from(getActivity())
                .setType(mineType)
                .setText(getString(R.string.share_from)
                        + mMovie.getTitle()
                        + getString(R.string.share_release_date)
                        + mMovie.getRelease_date()
                        + getString(R.string.share_overview)
                        + mMovie.getOverview())
                .setChooserTitle(title)
                .startChooser();
    }
}
