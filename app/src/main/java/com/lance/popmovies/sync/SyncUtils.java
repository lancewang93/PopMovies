package com.lance.popmovies.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class SyncUtils {
    private static final int AUTO_SYNC_INTERVAL_MINUTES = 15;
    private static final int AUTO_SYNC_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(AUTO_SYNC_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = AUTO_SYNC_INTERVAL_SECONDS;

    private static final String SYNC_JOB_TAG = "auto_sync_movie_list_job_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {

        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(SyncMovieListFirebaseJobService.class)
                .setTag(SYNC_JOB_TAG)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        AUTO_SYNC_INTERVAL_SECONDS,
                        AUTO_SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);

        sInitialized = true;
    }

}
