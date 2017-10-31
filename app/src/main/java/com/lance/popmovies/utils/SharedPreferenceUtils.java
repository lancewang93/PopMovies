package com.lance.popmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class SharedPreferenceUtils {

    private static final String KEY_MAIN_SORT_BY = "key_main_sort_by";

    public static void setMainSortBy(Context context, int sortByNumber) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_MAIN_SORT_BY, sortByNumber);
        editor.apply();
    }

    public static int getMainSortBy(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(KEY_MAIN_SORT_BY, 0);
    }
}
