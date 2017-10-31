package com.lance.popmovies.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.bean.Review;
import com.lance.popmovies.bean.Trailer;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class DetailLoader extends AsyncTaskLoader<Movie> {
    private Movie mMovie;

    public DetailLoader(Context context, Movie movie) {
        super(context);
        mMovie = movie;
    }

    @Override
    protected void onStartLoading() {
            forceLoad();
    }

    @Override
    public Movie loadInBackground() {
        List<Trailer> trailers = MovieListUtils.loadTrailerListFromNet(mMovie.getId());
        mMovie.setTrailers(trailers);
        List<Review> reviews = MovieListUtils.loadReviewListFromNet(mMovie.getId());
        mMovie.setReviews(reviews);
        boolean isFavorite = FavoriteUtils.isFavorite(getContext(), mMovie);
        mMovie.setFavorite(isFavorite);
        return mMovie;
    }
}
