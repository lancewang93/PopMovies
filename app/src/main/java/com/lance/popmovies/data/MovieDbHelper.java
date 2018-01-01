package com.lance.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lance.popmovies.data.MovieContract.FavoriteEntry;
import com.lance.popmovies.data.MovieContract.PopularEntry;
import com.lance.popmovies.data.MovieContract.TopRatedEntry;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static MovieDbHelper instance;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "popmovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MovieDbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieDbHelper.class) {
                if (instance == null) {
                    instance = new MovieDbHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建流行的表
        String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " + PopularEntry.TABLE_NAME +
                " ("
                + MovieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_DATE + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);

        //创建高评分的表
        String SQL_CREATE_TOP_RATED_TABLE = "CREATE TABLE " + TopRatedEntry.TABLE_NAME +
                " ("
                + MovieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_DATE + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);

        //创建收藏的表
        String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME +
                " ("
                + MovieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_MOVIE_DATE + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("ALTER  TABLE " + PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("ALTER  TABLE " + TopRatedEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("ALTER  TABLE " + FavoriteEntry.TABLE_NAME);
    }
}
