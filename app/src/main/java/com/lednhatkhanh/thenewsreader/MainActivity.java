package com.lednhatkhanh.thenewsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lednhatkhanh.thenewsreader.databinding.ActivityMainBinding;
import com.lednhatkhanh.thenewsreader.utils.DataUtils;
import com.lednhatkhanh.thenewsreader.utils.NetworkUtils;
import com.lednhatkhanh.thenewsreader.utils.PreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
    implements NewsAdapter.NewsAdapterOnClickHandler,
        LoaderCallbacks<JSONArray>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    ActivityMainBinding mBinding;
    private NewsAdapter mNewsAdapter;
    private static final int NEWS_LOADER_ID = 0;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
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

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(PREFERENCES_HAVE_BEEN_UPDATED) {
            getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
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
                    showLoading();
                    forceLoad();
                }
            }

            @Override
            public JSONArray loadInBackground() {
                OkHttpClient client = new OkHttpClient();

                String sourceParam = PreferencesUtils.getSource(MainActivity.this);
                String sortByParam = PreferencesUtils.getSortBy(MainActivity.this);

                Uri mNewsUri = new Uri.Builder()
                        .scheme("https")
                        .authority("newsapi.org")
                        .appendPath("v1")
                        .appendPath("articles")
                        .appendQueryParameter("source", sourceParam)
                        .appendQueryParameter("sortBy", sortByParam)
                        .appendQueryParameter("apiKey", NetworkUtils.API_KEY)
                        .build();
                Log.i(LOG_TAG, mNewsUri.toString());

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
        if(data == null) {
            showError();
        } else {
            try {
                mNewsAdapter.setArticlesList(DataUtils.convertArticlesJsonArrayToArrayList(data));
                showResult();
            } catch (JSONException | ParseException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                showError();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(String title) {
        Intent startDetailActivityIntent = new Intent(this, DetailActivity.class);
        startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, title);
        startActivity(startDetailActivityIntent);
    }

    private void showLoading() {
        mBinding.errorTextView.setVisibility(View.INVISIBLE);
        mBinding.loadingIndicator.setVisibility(View.VISIBLE);
        mBinding.newsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showResult() {
        mBinding.errorTextView.setVisibility(View.INVISIBLE);
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.newsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        mBinding.errorTextView.setVisibility(View.VISIBLE);
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.newsRecyclerView.setVisibility(View.INVISIBLE);
    }
}
