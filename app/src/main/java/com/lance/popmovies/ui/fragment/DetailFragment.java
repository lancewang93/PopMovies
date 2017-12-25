package com.lance.popmovies.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lance.popmovies.R;
import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.ui.adapter.ReviewAdapter;
import com.lance.popmovies.ui.adapter.TrailerAdapter;
import com.lance.popmovies.utils.DetailLoader;
import com.lance.popmovies.utils.FavoriteUtils;
import com.lance.popmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class DetailFragment extends Fragment implements
        TrailerAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<Movie> {

    private static final int DETAIL_MOVIE_LOADER = 5;

    public static final String ARG_MOVIE = "movie";

    private ImageView mBackdropImageView;
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;

    private TextView mErrorTextView;
    private ContentLoadingProgressBar mDetailLoading;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private FloatingActionButton mDetailFloatingActionButton;

    private Movie mMovie;
    private boolean isFavorite;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mErrorTextView = view.findViewById(R.id.tv_error);
        mDetailLoading = view.findViewById(R.id.pb_detail_loading);

        mBackdropImageView = view.findViewById(R.id.iv_detail_backdrop);
        mTitleTextView = view.findViewById(R.id.tv_detail_title);
        mPosterImageView = view.findViewById(R.id.iv_detail_poster);
        mReleaseDateTextView = view.findViewById(R.id.tv_detail_release_date);
        mVoteAverageTextView = view.findViewById(R.id.tv_detail_vote_average);
        mOverviewTextView = view.findViewById(R.id.tv_detail_overview);
        mTrailerRecyclerView = view.findViewById(R.id.rcv_detail_trailer);
        mTrailerAdapter = new TrailerAdapter(getContext(), this);
        mReviewRecyclerView = view.findViewById(R.id.rcv_detail_review);
        mReviewAdapter = new ReviewAdapter(getContext());
        mDetailFloatingActionButton = view.findViewById(R.id.fab_detail);

        if (NetworkUtils.isNetworkAvailableAndConnected(getContext())) {
            initDetail();
        } else {
            showErrorView();
        }

        return view;
    }


    private void initDetail() {
        getActivity().getSupportLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + mMovie.getTrailers().get(clickedItemIndex).getVideoKey()));
        startActivity(intent);
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        mDetailLoading.setVisibility(View.VISIBLE);
        if (id == DETAIL_MOVIE_LOADER) {
            return new DetailLoader(getContext(), mMovie);
        } else {
            throw new IllegalArgumentException("unknown loader id : " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        if (data.isFavorite()) {
            isFavorite = data.isFavorite();
        }

        mMovie = data;

        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w780/" + mMovie.getBackdrop_path())
                .into(mBackdropImageView);

        mTitleTextView.setText(mMovie.getTitle());
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/" + mMovie.getPoster_path())
                .into(mPosterImageView);
        mReleaseDateTextView.setText(mMovie.getRelease_date());
        mVoteAverageTextView.setText(String.format("%2.1f", mMovie.getVote_average()));
        mOverviewTextView.setText(mMovie.getOverview());

        if (data.getTrailers() != null && !data.getTrailers().isEmpty()) {
            LinearLayoutManager trailerLM = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mTrailerRecyclerView.setLayoutManager(trailerLM);
            mTrailerRecyclerView.setHasFixedSize(true);
            mTrailerAdapter.refreshTrailerList(data.getTrailers());
            mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        } else {
            Toast.makeText(getContext(), "无预告片数据", Toast.LENGTH_SHORT).show();
        }

        if (data.getReviews() != null && !data.getReviews().isEmpty()) {
            LinearLayoutManager reviewLM = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mReviewRecyclerView.setLayoutManager(reviewLM);
            mReviewRecyclerView.setHasFixedSize(true);
            mReviewAdapter.refreshReviewList(data.getReviews());
            mReviewRecyclerView.setAdapter(mReviewAdapter);
        } else {
            Toast.makeText(getContext(), "无评论数据", Toast.LENGTH_SHORT).show();
        }

        mDetailLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        mTrailerAdapter.refreshTrailerList(null);
    }

    private void showErrorView() {
        mTitleTextView.setVisibility(View.INVISIBLE);
        mPosterImageView.setVisibility(View.INVISIBLE);
        mReleaseDateTextView.setVisibility(View.INVISIBLE);
        mVoteAverageTextView.setVisibility(View.INVISIBLE);
        mOverviewTextView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mDetailFloatingActionButton.setVisibility(View.INVISIBLE);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.detail, menu);
//        if (isFavorite) {
//            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_action_favorite);
//        } else {
//            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_action_unfavorite);
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_favorite:
//                favorite();
//                if (isFavorite) {
//                    item.setIcon(R.drawable.ic_action_unfavorite);
//                    isFavorite = false;
//                } else {
//                    item.setIcon(R.drawable.ic_action_favorite);
//                    isFavorite = true;
//                }
//                return true;
//            case R.id.action_share:
//                share();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void favorite() {
        FavoriteUtils.dealWithFavorite(getContext(), mMovie);
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
                        + getString(R.string.share_vote_average)
                        + mMovie.getVote_average()
                        + getString(R.string.share_trailer)
                        + mMovie.getTrailers().get(0).getVideoKey())
                .setChooserTitle(title)
                .startChooser();
    }
}
