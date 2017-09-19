package com.lance.popmovies.db;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public final class MoviesContract {
    public MoviesContract() {
    }

    public static class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_VOTE = "vote";
        public static final String COLUMN_MOVIE_DATE = "date";
    }

}
