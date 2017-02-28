package com.lednhatkhanh.thenewsreader.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.lednhatkhanh.thenewsreader.data.NewsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by lednh on 2/27/2017.
 */

public class JsonUtils {
    public static ContentValues[] getContentValuesFromJson(Context context, String jsonStr) {
        try {
            JSONObject newsJsonObject = new JSONObject(jsonStr);

            JSONArray jsonNewsArray = newsJsonObject.getJSONArray("articles");

            ContentValues[] mContentValuesArray = new ContentValues[jsonNewsArray.length()];

            for(int i=0; i < jsonNewsArray.length(); i++) {
                ContentValues contentValues = new ContentValues();
                JSONObject articleJsonObject = jsonNewsArray.getJSONObject(i);
                String rawTime = articleJsonObject.getString("publishedAt");
                long timeInNumber = DataUtils.convertDateTimeToNumber(rawTime);

                contentValues.put(NewsContract.NewsEntry.COLUMN_TITLE,
                        articleJsonObject.getString("title"));
                //Log.i("DATA", "TITLE:" + articleJsonObject.getString("title"));
                contentValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR,
                        articleJsonObject.getString("author"));
                //Log.i("DATA", "AUTHOR:" + articleJsonObject.getString("author"));
                contentValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION,
                        articleJsonObject.getString("description"));
                //Log.i("DATA", "DESCRIPTION:" + articleJsonObject.getString("description"));
                contentValues.put(NewsContract.NewsEntry.COLUMN_URL,
                        articleJsonObject.getString("url"));
                //Log.i("DATA", "URL:" + articleJsonObject.getString("url"));
                contentValues.put(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE,
                        articleJsonObject.getString("urlToImage"));
                //Log.i("DATA", "URLTOIMAGE:" + articleJsonObject.getString("urlToImage"));
                contentValues.put(NewsContract.NewsEntry.COLUMN_PUBLISHED_AT,
                        timeInNumber);
                //Log.i("DATA", "TIME:" + timeInNumber);

                mContentValuesArray[i] = contentValues;
            }

            return mContentValuesArray;
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
