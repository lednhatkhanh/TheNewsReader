package com.lednhatkhanh.thenewsreader;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lednhatkhanh.thenewsreader.utils.DataUtils;
import com.lednhatkhanh.thenewsreader.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
    implements NewsAdapter.NewsAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorTextView;

    private NewsAdapter mNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.newsRecyclerView);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loadingIndicator);
        mErrorTextView = (TextView) findViewById(R.id.errorTextView);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter(this);
        mRecyclerView.setAdapter(mNewsAdapter);

        new FetchNewsTask().execute();
    }

    @Override
    public void onClick(String title) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void showResult() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private class FetchNewsTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected void onPreExecute() {
            showLoadingIndicator();
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
                showError();
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                showError();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray == null) return;

            Log.i(LOG_TAG, jsonArray.toString());

            try {
                mNewsAdapter.setArticlesList(DataUtils.convertArticlesJsonArrayToArrayList(jsonArray));
                showResult();
            } catch (JSONException e) {
                e.printStackTrace();
                showError();
            }
        }
    }
}
