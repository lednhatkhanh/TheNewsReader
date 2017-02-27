package com.lednhatkhanh.thenewsreader;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lednhatkhanh.thenewsreader.databinding.ActivityMainBinding;
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
    implements NewsAdapter.NewsAdapterOnClickHandler,
        LoaderCallbacks<JSONArray> {

    ActivityMainBinding mBinding;
    private NewsAdapter mNewsAdapter;
    private static final int NEWS_LOADER_ID = 0;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.newsRecyclerView.setLayoutManager(linearLayoutManager);
        mBinding.newsRecyclerView.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter(this);
        mBinding.newsRecyclerView.setAdapter(mNewsAdapter);

        int loaderId = NEWS_LOADER_ID;
        LoaderCallbacks<JSONArray> callbacks = MainActivity.this;

        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callbacks);
    }

    @Override
    public Loader<JSONArray> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<JSONArray>(this) {
            JSONArray articlesJsonArray = null;

            @Override
            protected void onStartLoading() {
                if(articlesJsonArray != null) {
                    deliverResult(articlesJsonArray);
                } else {
                    mBinding.loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public JSONArray loadInBackground() {
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
            public void deliverResult(JSONArray data) {
                articlesJsonArray = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray data) {
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        try {
            mNewsAdapter.setArticlesList(DataUtils.convertArticlesJsonArrayToArrayList(data));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
            showError();
        }

        if(data == null) {
            showError();
        } else {
            showResult();
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }

    @Override
    public void onClick(String title) {
        Intent startDetailActivityIntent = new Intent(this, DetailActivity.class);
        startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, title);
        startActivity(startDetailActivityIntent);
    }

    private void showResult() {
        mBinding.errorTextView.setVisibility(View.INVISIBLE);
        mBinding.newsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        mBinding.errorTextView.setVisibility(View.INVISIBLE);
        mBinding.newsRecyclerView.setVisibility(View.VISIBLE);
    }
}
