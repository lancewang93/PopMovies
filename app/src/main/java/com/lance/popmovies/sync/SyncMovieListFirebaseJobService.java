package com.lance.popmovies.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class SyncMovieListFirebaseJobService extends JobService {

    private AsyncTask mBackgroundAsyncTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mBackgroundAsyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = SyncMovieListFirebaseJobService.this;
                SyncMovieListTask.executeTask(context, SyncMovieListTask.ACTION_SYNC_POPULAR_MOVIES);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, false);
            }
        }.execute();
        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if (mBackgroundAsyncTask != null) mBackgroundAsyncTask.cancel(true);
        return true;
    }
}
