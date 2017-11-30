package com.lance.popmovies.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.data.MovieContract;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class FavoriteUtils {

//    private static FavoriteUtils sInstance;
//
//    private FavoriteUtils(Context context) {
//
//    }
//
//    public static FavoriteUtils getInstance(Context context) {
//        if (sInstance == null) {
//            sInstance = new FavoriteUtils(context);
//        }
//        return sInstance;
//    }

    //收藏电影
    public static void saveMovieToFavorite(Context context, Movie movie) {
        ContentValues value = new ContentValues();
        value.put(MovieContract.COLUMN_MOVIE_ID, movie.getId());
        value.put(MovieContract.COLUMN_MOVIE_TITLE, movie.getTitle());
        value.put(MovieContract.COLUMN_MOVIE_POSTER, movie.getPoster_path());
        value.put(MovieContract.COLUMN_MOVIE_BACKDROP, movie.getBackdrop_path());
        value.put(MovieContract.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        value.put(MovieContract.COLUMN_MOVIE_AVERAGE, movie.getVote_average());
        value.put(MovieContract.COLUMN_MOVIE_DATE, movie.getRelease_date());

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MovieContract.FavoriteEntry.CONTENT_URI, value);
        if (Long.valueOf(uri.getLastPathSegment()) >= 0) {
            Log.d("tag", "favorite work");
        } else {
            Log.d("tag", "favorite noWork");
        }
    }

    //取消收藏
    public static void cancelMovieToFavorite(Context context, Movie movie) {
        ContentResolver resolver = context.getContentResolver();
        Uri movieIdUri = MovieContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(movie.getId() + "").build();
        resolver.delete(movieIdUri, null, null);
    }

    //是否是收藏的电影
    public static boolean isFavorite(Context context, Movie movie) {
        boolean isFavorite = false;
        ContentResolver resolver = context.getContentResolver();
        Uri movieIdUri = MovieContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(movie.getId() + "").build();
        Cursor cursor = resolver.query(
                movieIdUri,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            isFavorite = true;
        }
        cursor.close();
        return isFavorite;
    }
}
