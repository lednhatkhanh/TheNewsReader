package com.lednhatkhanh.thenewsreader;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lednhatkhanh.thenewsreader.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FetchNewsTask().execute();
    }

    private class FetchNewsTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            Uri mNewsUri = new Uri.Builder()
                    .scheme("https")
                    .authority("newsapi.org")
                    .appendPath("v1")
                    .appendPath("articles")
                    .appendQueryParameter("source", "the-next-web")
                    .appendQueryParameter("sortBy", "latest")
                    .appendQueryParameter("apiKey", NetworkUtils.API_KEY)
                    .build();

            try {
                URL mNewsUrl = new URL(mNewsUri.toString());

                Request request = new Request.Builder()
                        .url(mNewsUrl)
                        .build();

                Response response = client.newCall(request).execute();
                JSONObject responseJsonObject = new JSONObject(response.body().string());
                return responseJsonObject.getJSONArray("articles");
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray == null) return;

            Log.i(LOG_TAG, jsonArray.toString());
        }
    }
}
