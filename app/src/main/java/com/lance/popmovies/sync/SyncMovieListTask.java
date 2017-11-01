package com.lance.popmovies.sync;

import android.content.Context;
import android.widget.Toast;

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

    private static void syncPopularMovies(Context context) {
        if (MovieListUtils.saveMovieListToLocal(context, context.getString(R.string.popular))) {
            Toast.makeText(context, "Sync Popular Movies is done", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sync Popular Movies isn't done", Toast.LENGTH_SHORT).show();
        }
    }

    private static void syncTopRatedMovies(Context context) {
        if (MovieListUtils.saveMovieListToLocal(context, context.getString(R.string.top_rated))) {
            Toast.makeText(context, "Sync Popular Movies is done", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sync Popular Movies isn't done", Toast.LENGTH_SHORT).show();
        }
    }


}
