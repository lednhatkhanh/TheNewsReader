package com.lednhatkhanh.thenewsreader.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.lednhatkhanh.thenewsreader.data.NewsContract;
import com.lednhatkhanh.thenewsreader.utils.JsonUtils;
import com.lednhatkhanh.thenewsreader.utils.NetworkUtils;

import java.net.URL;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsSyncTask {
    synchronized public static void syncNews(Context context) {
        URL newsRequestUrl = NetworkUtils.getUrl(context);

        String response = NetworkUtils.getResponseFromUrl(context, newsRequestUrl);

        ContentValues[] newsValues = JsonUtils.getContentValuesFromJson(context, response);

        if(newsValues != null && newsValues.length > 0) {
            ContentResolver contentResolver = context.getContentResolver();

            contentResolver.delete(
                    NewsContract.NewsEntry.CONTENT_URI,
                    null,
                    null);

            contentResolver.bulkInsert(NewsContract.NewsEntry.CONTENT_URI, newsValues);
        }
    }
}
