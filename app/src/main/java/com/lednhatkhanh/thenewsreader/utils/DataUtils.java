package com.lednhatkhanh.thenewsreader.utils;

import com.lednhatkhanh.thenewsreader.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by lednh on 2/27/2017.
 */

public class DataUtils {
    public static ArrayList<Article> convertArticlesJsonArrayToArrayList(JSONArray articleJsonArray)
            throws JSONException {
        ArrayList<Article> articlesArrayList = new ArrayList<>();

        for(int i=0;i< articleJsonArray.length(); i++) {
            JSONObject articleJsonObject = articleJsonArray.getJSONObject(i);
            String title = articleJsonObject.getString("title");
            String author = articleJsonObject.getString("author");
            String description = articleJsonObject.getString("description");
            String url = articleJsonObject.getString("url");
            String urlToImage = articleJsonObject.getString("urlToImage");
            String publishedAt = articleJsonObject.getString("publishedAt");

            articlesArrayList.add(new Article(title, author, description, url, urlToImage, publishedAt));
        }

        return articlesArrayList;
    }

    public static String convertUTCToLocalTime(String rawTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return new SimpleDateFormat("HH:mm MM-dd-YYYY", Locale.getDefault())
                .format(dateFormat.parse(rawTime));
    }
}
