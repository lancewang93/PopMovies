package com.lance.popmovies.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lance.popmovies.R;
import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.bean.Review;
import com.lance.popmovies.bean.Trailer;
import com.lance.popmovies.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class MovieListUtils {

    //从网络获取电影列表(流行、高分)
    public static List<Movie> loadMovieListFromNet(Context context, int movieType) {
        String jsonData = "";
        switch (movieType) {
            case 0:
                jsonData = NetworkUtils.sendMainRequest(context.getString(R.string.popular));
                break;
            case 1:
                jsonData = NetworkUtils.sendMainRequest(context.getString(R.string.top_rated));
                break;
            default:
                break;
        }

        return JSONDataUtils.parseJSONDataToMovieList(jsonData);
    }

    //从本地获取电影列表(流行、高分、收藏)
    public static List<Movie> loadMovieListFromLocal(Context context, int movieType) {
        Cursor cursor = null;
        switch (movieType) {
            case 0:
                cursor = context.getContentResolver().query(
                        MovieContract.PopularEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
                break;
            case 1:
                cursor = context.getContentResolver().query(
                        MovieContract.TopRatedEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
                break;
            case 2:
                cursor = context.getContentResolver().query(
                        MovieContract.FavoriteEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
                break;
        }


        if (cursor == null || cursor.getColumnCount() == 0) {
            return null;
        }

        List<Movie> movies = new ArrayList<>();

        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.COLUMN_MOVIE_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_MOVIE_TITLE)));
            movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_MOVIE_BACKDROP)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_MOVIE_OVERVIEW)));
            movie.setVote_average(cursor.getDouble(cursor.getColumnIndex(MovieContract.COLUMN_MOVIE_AVERAGE)));
            movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_MOVIE_DATE)));
            movie.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_MOVIE_POSTER)));
            movies.add(movie);
        }

        cursor.close();
        return movies;
    }

    //从网络获取电影的预告片列表
    public static List<Trailer> loadTrailerListFromNet(int movieId) {
        String jsonData = NetworkUtils.sendTrailerRequest(movieId);
        return JSONDataUtils.parseJSONDataToTrailerList(jsonData);
    }

    //从网络获取电影的评论列表
    public static List<Review> loadReviewListFromNet(int movieId) {
        String jsonData = NetworkUtils.sendReviewRequest(movieId);
        return JSONDataUtils.parseJSONDataToReviewList(jsonData);
    }

    //存储电影列表至本地(流行、高分)
    public static boolean saveMovieListToLocal(Context context, String movieType) {
        List<Movie> movies = new ArrayList<>();

        if (movieType.equals(context.getString(R.string.popular))) {
            movies = loadMovieListFromNet(context, 0);
        } else if (movieType.equals(context.getString(R.string.top_rated))) {
            movies = loadMovieListFromNet(context, 1);
        }

        if (movies != null && !movies.isEmpty()) {
            List<ContentValues> valueList = new ArrayList<>();
            for (int i = 0; i < movies.size(); i++) {
                ContentValues value = new ContentValues();
                value.put(MovieContract.COLUMN_MOVIE_ID, movies.get(i).getId());
                value.put(MovieContract.COLUMN_MOVIE_TITLE, movies.get(i).getTitle());
                value.put(MovieContract.COLUMN_MOVIE_AVERAGE, movies.get(i).getVote_average());
                value.put(MovieContract.COLUMN_MOVIE_BACKDROP, movies.get(i).getBackdrop_path());
                value.put(MovieContract.COLUMN_MOVIE_DATE, movies.get(i).getRelease_date());
                value.put(MovieContract.COLUMN_MOVIE_OVERVIEW, movies.get(i).getOverview());
                value.put(MovieContract.COLUMN_MOVIE_POSTER, movies.get(i).getPoster_path());
                valueList.add(value);
            }
            ContentValues[] contentValueList = valueList.toArray(new ContentValues[0]);
            ContentResolver resolver = context.getContentResolver();

            if (movieType.equals(context.getString(R.string.popular))) {
                resolver.delete(MovieContract.PopularEntry.CONTENT_URI, null, null);
                resolver.bulkInsert(MovieContract.PopularEntry.CONTENT_URI, contentValueList);
                return true;
            } else if (movieType.equals(context.getString(R.string.top_rated))) {
                resolver.delete(MovieContract.TopRatedEntry.CONTENT_URI, null, null);
                resolver.bulkInsert(MovieContract.TopRatedEntry.CONTENT_URI, contentValueList);
                return true;
            } else if (movieType.equals(context.getString(R.string.favorite))) {
                resolver.delete(MovieContract.FavoriteEntry.CONTENT_URI, null, null);
                resolver.bulkInsert(MovieContract.FavoriteEntry.CONTENT_URI, contentValueList);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }
}
