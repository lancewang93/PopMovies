package com.lance.popmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class MovieListIntentService extends IntentService {

    public MovieListIntentService() {
        super("MovieListIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncMovieListTask.executeTask(this, intent.getAction());
    }
}
