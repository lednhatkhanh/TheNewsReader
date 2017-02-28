package com.lednhatkhanh.thenewsreader.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.lednhatkhanh.thenewsreader.data.NewsContract;

import java.util.concurrent.TimeUnit;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static final String THE_NEWS_READER_SYNC_TAG = "the-news-reader-sync";

    private static boolean sInitialized;

    private static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncNewsJob = dispatcher.newJobBuilder()
                .setService(NewsFirebaseJobService.class)
                .setTag(THE_NEWS_READER_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncNewsJob);
    }

    synchronized public static void initialize(@NonNull final Context context) {
        if(sInitialized) return;
        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = NewsContract.NewsEntry.CONTENT_URI;
                String[] projectionColumn = {NewsContract.NewsEntry._ID};
                Cursor cursor = context.getContentResolver().query(uri,
                        projectionColumn, null, null, null);

                if(cursor == null || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
            }
        });
        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intent = new Intent(context, NewsSyncIntentService.class);
        context.startService(intent);
    }
}
