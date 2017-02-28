package com.lednhatkhanh.thenewsreader.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.lednhatkhanh.thenewsreader.data.NewsContract;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsSyncUtils {

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context) {
        if(sInitialized) return;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Uri uri = NewsContract.NewsEntry.CONTENT_URI;
                String[] projectionColumn = {NewsContract.NewsEntry._ID};
                Cursor cursor = context.getContentResolver().query(uri,
                        projectionColumn, null, null, null);

                if(cursor == null || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
                return null;
            }
        }.execute();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intent = new Intent(context, NewsSyncIntentService.class);
        context.startService(intent);
    }
}
