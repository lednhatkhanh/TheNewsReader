package com.lednhatkhanh.thenewsreader.utils;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lednh on 2/27/2017.
 */

public class NetworkUtils {
    private static final String API_KEY = "cd17d3ef28f84e86b3b5ad9681b2930e";

    public static URL getUrl(Context context) {
        Uri mUri = new Uri.Builder()
                .scheme("https")
                .authority("newsapi.org")
                .appendPath("v1")
                .appendPath("articles")
                .appendQueryParameter("source", PreferencesUtils.getSource(context))
                .appendQueryParameter("sortBy", PreferencesUtils.getSortBy(context))
                .appendQueryParameter("apiKey", API_KEY)
                .build();
        try {
            return new URL(mUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromUrl(Context context, URL url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request)
                    .execute();

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
