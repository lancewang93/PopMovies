package com.lance.popmovies.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lance.popmovies.R;
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
//        if (mRequestType == 2) {
//            mMovies = MovieListUtils.loadMovieListFromLocal(mContext, mRequestType);
//        } else {
//            if (NetworkUtils.isNetworkAvailableAndConnected(mContext)) {
//                mMovies = MovieListUtils.loadMovieListFromNet(mContext, mRequestType);
//            } else {
//                mMovies = MovieListUtils.loadMovieListFromLocal(mContext, mRequestType);
//            }
//        }

        //有网络 + 流行/高分 = 先存下来
        if (NetworkUtils.isNetworkAvailableAndConnected(getContext())) {
            if (mRequestType == 0) {
                MovieListUtils.saveMovieListToLocal(getContext(), getContext().getString(R.string.popular));
            } else if (mRequestType == 1) {
                MovieListUtils.saveMovieListToLocal(getContext(), getContext().getString(R.string.top_rated));
            }
        }
        mMovies = MovieListUtils.loadMovieListFromLocal(getContext(), mRequestType);
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
