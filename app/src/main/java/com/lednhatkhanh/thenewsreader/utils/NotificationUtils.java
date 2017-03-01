package com.lednhatkhanh.thenewsreader.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.lednhatkhanh.thenewsreader.DetailActivity;
import com.lednhatkhanh.thenewsreader.R;
import com.lednhatkhanh.thenewsreader.data.NewsContract;

/**
 * Created by lednh on 2/28/2017.
 */

public class NotificationUtils {
    public static final String[] NEWS_NOTIFICATION_PROJECTION = {
            NewsContract.NewsEntry._ID,
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsContract.NewsEntry.COLUMN_AUTHOR,
    };

    public static final int INDEX_ARTICLE_ID = 0;
    public static final int INDEX_ARTICLE_TTITLE = 1;
    public static final int INDEX_ARTICLE_AUTHOR = 2;

    private static final int NEWS_NOTIFICATION_ID = 1000;

    /**
     * Create notification for the latest article
     * @param context The application's context
     */
    public static void notifyLatestNews(@NonNull final Context context) {
        Uri articlesUri = NewsContract.NewsEntry.CONTENT_URI;

        Cursor articlesCursor = context.getContentResolver().query(
                articlesUri,
                NEWS_NOTIFICATION_PROJECTION,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_PUBLISHED_AT + " DESC");

        if(articlesCursor.moveToFirst()) {
            long articleId = articlesCursor.getLong(INDEX_ARTICLE_ID);
            String articleTitle = articlesCursor.getString(INDEX_ARTICLE_TTITLE);
            String articleAuthor = articlesCursor.getString(INDEX_ARTICLE_AUTHOR);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_sms_black_24dp)
                    .setContentText("By " + articleAuthor)
                    .setContentTitle(articleTitle)
                    .setAutoCancel(true);

            Intent intentForLatestArticle = new Intent(context, DetailActivity.class);
            intentForLatestArticle.setData(NewsContract.NewsEntry.buildUriWithId(articleId));

            TaskStackBuilder taskStackBuilder  = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intentForLatestArticle);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(NEWS_NOTIFICATION_ID, notificationBuilder.build());

            PreferencesUtils.saveLastNotificationTime(context, System.currentTimeMillis());
        }

        articlesCursor.close();
    }
}
