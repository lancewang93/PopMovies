package com.lance.popmovies.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lance.popmovies.bean.Movie;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class MainLoader extends AsyncTaskLoader<List<Movie>> {

    private int mRequestType;

    private List<Movie> mMovies;

    public MainLoader(Context context, int requestType) {
        super(context);
        mRequestType = requestType;
    }

    @Override
    protected void onStartLoading() {
        if (mMovies != null) {
            deliverResult(mMovies);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mRequestType == 2) {
            mMovies = MovieListUtils.loadMovieListFromLocal(getContext(), mRequestType);
        } else {
            if (NetworkUtils.isNetworkAvailableAndConnected(getContext())) {
                mMovies = MovieListUtils.loadMovieListFromNet(getContext(), mRequestType);
            } else {
                mMovies = MovieListUtils.loadMovieListFromLocal(getContext(), mRequestType);
            }
        }
        return mMovies;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        mMovies = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
