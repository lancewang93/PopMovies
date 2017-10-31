package com.lance.popmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public final class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.lance.popmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //三个电影表的常量
    public static final String _ID = BaseColumns._ID;
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_TITLE = "title";
    public static final String COLUMN_MOVIE_POSTER = "poster";
    public static final String COLUMN_MOVIE_BACKDROP = "backdrop";
    public static final String COLUMN_MOVIE_OVERVIEW = "overview";
    public static final String COLUMN_MOVIE_AVERAGE = "average";
    public static final String COLUMN_MOVIE_DATE = "date";

    //流行电影的表的常量
    public static class PopularEntry implements BaseColumns {

        public static final String TABLE_NAME = "popular";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();
    }

    //高分电影的表的常量
    public static class TopRatedEntry implements BaseColumns {
        public static final String TABLE_NAME = "top_rated";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();
    }

    //收藏的电影的表的常量
    public static class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();
    }
}
