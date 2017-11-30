package com.lance.popmovies.sync;

import android.content.Context;
import android.util.Log;

import com.lance.popmovies.R;
import com.lance.popmovies.utils.MovieListUtils;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class SyncMovieListTask {

    public static final String ACTION_SYNC_POPULAR_MOVIES = "sync-popular-movies";
    public static final String ACTION_SYNC_TOP_RATED_MOVIES = "sync-top-rated-movies";


    public static void executeTask(Context context, String action) {
        if (ACTION_SYNC_POPULAR_MOVIES.equals(action)) {
            syncPopularMovies(context);
        } else if (ACTION_SYNC_TOP_RATED_MOVIES.equals(action)) {
            syncTopRatedMovies(context);
        }
    }

    synchronized private static void syncPopularMovies(Context context) {
        if (MovieListUtils.saveMovieListToLocal(context, context.getString(R.string.popular))) {
            Log.d("tag", "syncPopularMovies: " + "Sync Popular Movies is done");
        } else {
            Log.d("tag", "syncPopularMovies: " + "Sync Popular Movies is failed");
        }
    }

    synchronized private static void syncTopRatedMovies(Context context) {
        if (MovieListUtils.saveMovieListToLocal(context, context.getString(R.string.top_rated))) {
            Log.d("tag", "syncTopRatedMovies: " + "Sync Top Rated Movies is done");
        } else {
            Log.d("tag", "syncTopRatedMovies: " + "Sync Top Rated Movies is failed");
        }
    }


}
