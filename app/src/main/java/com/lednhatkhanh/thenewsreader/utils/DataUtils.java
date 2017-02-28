package com.lednhatkhanh.thenewsreader.utils;

import android.util.Log;

import com.lednhatkhanh.thenewsreader.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lednh on 2/27/2017.
 */

public class DataUtils {

    public static ArrayList<Article> convertArticlesJsonArrayToArrayList(JSONArray articleJsonArray)
            throws JSONException, ParseException {
        ArrayList<Article> articlesArrayList = new ArrayList<>();

        for(int i=0;i< articleJsonArray.length(); i++) {
            JSONObject articleJsonObject = articleJsonArray.getJSONObject(i);
            String title = articleJsonObject.getString("title");
            String author = articleJsonObject.getString("author");
            String description = articleJsonObject.getString("description");
            String url = articleJsonObject.getString("url");
            String urlToImage = articleJsonObject.getString("urlToImage");
            long publishedAt = convertDateTimeToNumber(articleJsonObject.getString("publishedAt"));

            articlesArrayList.add(new Article(title, author, description, url, urlToImage, publishedAt));
        }

        return articlesArrayList;
    }

    static long convertDateTimeToNumber(String rawTime) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .parse(rawTime).getTime();
    }

    public static String getReadableDateFormat(long timeInMillis) {
        return new SimpleDateFormat("HH:mm MM-dd-yyyy", Locale.getDefault())
                .format(new Date(timeInMillis));
    }
}
