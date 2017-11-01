package com.lance.popmovies.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class MovieListIntentService extends IntentService {

    public MovieListIntentService() {
        super("MovieListIntentService");
    }

    public static Intent newIntentService(Context context, String action) {
        Intent intent = new Intent(context, MovieListIntentService.class);
        intent.setAction(action);
        return intent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        SyncMovieListTask.executeTask(this, action);
    }
}
