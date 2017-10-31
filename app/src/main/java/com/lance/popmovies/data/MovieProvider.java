package com.lance.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_POPULAR = 100;
    public static final int CODE_POPULAR_WITH_ID = 101;
    public static final int CODE_TOP_RATED = 200;
    public static final int CODE_TOP_RATED_WITH_ID = 201;
    public static final int CODE_FAVORITE = 300;
    public static final int CODE_FAVORITE_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDbHelper mMovieDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PopularEntry.TABLE_NAME, CODE_POPULAR);
        matcher.addURI(authority, MovieContract.PopularEntry.TABLE_NAME + "/#", CODE_POPULAR_WITH_ID);

        matcher.addURI(authority, MovieContract.TopRatedEntry.TABLE_NAME, CODE_TOP_RATED);
        matcher.addURI(authority, MovieContract.TopRatedEntry.TABLE_NAME + "/#", CODE_TOP_RATED_WITH_ID);

        matcher.addURI(authority, MovieContract.FavoriteEntry.TABLE_NAME, CODE_FAVORITE);
        matcher.addURI(authority, MovieContract.FavoriteEntry.TABLE_NAME + "/#", CODE_FAVORITE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = MovieDbHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_POPULAR:
                cursor = db.query(MovieContract.PopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            case CODE_POPULAR_WITH_ID:
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(MovieContract.PopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            case CODE_TOP_RATED:
                cursor = db.query(MovieContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            case CODE_TOP_RATED_WITH_ID:
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(MovieContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            case CODE_FAVORITE:
                cursor = db.query(MovieContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            case CODE_FAVORITE_WITH_ID:
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(MovieContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            default:
                throw new IllegalArgumentException("unknown's uri!");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        String tableName ;
        switch (sUriMatcher.match(uri)) {
            case CODE_POPULAR:
                tableName = MovieContract.PopularEntry.TABLE_NAME;
                break;
            case CODE_TOP_RATED:
                tableName = MovieContract.TopRatedEntry.TABLE_NAME;
                break;
            case CODE_FAVORITE:
                tableName = MovieContract.FavoriteEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("unknown uri!");
        }

        long id = db.insert(tableName, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri.buildUpon().appendPath(id + "").build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        String tableName ;

        if (selection == null) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_POPULAR:
                tableName = MovieContract.PopularEntry.TABLE_NAME;
                break;
            case CODE_POPULAR_WITH_ID:
                tableName = MovieContract.PopularEntry.TABLE_NAME;
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            case CODE_TOP_RATED:
                tableName = MovieContract.TopRatedEntry.TABLE_NAME;
                break;
            case CODE_TOP_RATED_WITH_ID:
                tableName = MovieContract.TopRatedEntry.TABLE_NAME;
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            case CODE_FAVORITE:
                tableName = MovieContract.FavoriteEntry.TABLE_NAME;
                break;
            case CODE_FAVORITE_WITH_ID:
                tableName = MovieContract.FavoriteEntry.TABLE_NAME;
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            default:
                throw new IllegalArgumentException("unknow's uri!");
        }
        int deleteRowCount = db.delete(tableName, selection, selectionArgs);

        if (deleteRowCount > 0) getContext().getContentResolver().notifyChange(uri, null);

        return deleteRowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        String tableName ;
        switch (sUriMatcher.match(uri)) {
            case CODE_POPULAR:
                tableName = MovieContract.PopularEntry.TABLE_NAME;
                break;
            case CODE_POPULAR_WITH_ID:
                tableName = MovieContract.PopularEntry.TABLE_NAME;
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            case CODE_TOP_RATED:
                tableName = MovieContract.TopRatedEntry.TABLE_NAME;
                break;
            case CODE_TOP_RATED_WITH_ID:
                tableName = MovieContract.TopRatedEntry.TABLE_NAME;
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            case CODE_FAVORITE:
                tableName = MovieContract.FavoriteEntry.TABLE_NAME;
                break;
            case CODE_FAVORITE_WITH_ID:
                tableName = MovieContract.FavoriteEntry.TABLE_NAME;
                selection = MovieContract.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            default:
                throw new IllegalArgumentException("unknow's uri!");
        }

        int updateRowCount = db.update(tableName, contentValues, selection, selectionArgs);

        if (updateRowCount > 0) getContext().getContentResolver().notifyChange(uri, null);

        return updateRowCount;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_POPULAR:
                tableName = MovieContract.PopularEntry.TABLE_NAME;
                break;
            case CODE_TOP_RATED:
                tableName = MovieContract.TopRatedEntry.TABLE_NAME;
                break;
            case CODE_FAVORITE:
                tableName = MovieContract.FavoriteEntry.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        int insertRowCount = 0;
        db.beginTransaction();
        try {
            for (ContentValues value :
                    values) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1) insertRowCount++;
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        if (insertRowCount>0) getContext().getContentResolver().notifyChange(uri, null);

        return insertRowCount;
    }
}
