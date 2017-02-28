package com.lednhatkhanh.thenewsreader;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lednhatkhanh.thenewsreader.data.NewsContract;
import com.lednhatkhanh.thenewsreader.databinding.ActivityMainBinding;
import com.lednhatkhanh.thenewsreader.sync.NewsSyncUtils;

public class MainActivity extends AppCompatActivity
    implements NewsAdapter.NewsAdapterOnClickHandler,
        LoaderCallbacks<Cursor> {

    ActivityMainBinding mBinding;
    private NewsAdapter mNewsAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int NEWS_LOADER_ID = 44;
    public static final String[] MAIN_ARTICLE_PROJECTION = {
            NewsContract.NewsEntry._ID,
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsContract.NewsEntry.COLUMN_AUTHOR,
            NewsContract.NewsEntry.COLUMN_DESCRIPTION,
            NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE,
            NewsContract.NewsEntry.COLUMN_PUBLISHED_AT
    };
    public static final int INDEX_ARTICLE_ID = 0;
    public static final int INDEX_ARTICLE_TITLE = 1;
    public static final int INDEX_ARTICLE_AUTHOR = 2;
    public static final int INDEX_ARTICLE_DESCRIPTION = 3;
    public static final int INDEX_ARTICLE_URL_TO_IMAGE = 4;
    public static final int INDEX_ARTICLE_PUBLISHED_AT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.newsRecyclerView.setLayoutManager(linearLayoutManager);
        mBinding.newsRecyclerView.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter(this, this);
        mBinding.newsRecyclerView.setAdapter(mNewsAdapter);

        showLoading();

        getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

        NewsSyncUtils.startSynching(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case NEWS_LOADER_ID:
                Uri newsQueryUri = NewsContract.NewsEntry.CONTENT_URI;
                Log.i("DATA", newsQueryUri.toString());
                return new CursorLoader(this,
                        newsQueryUri,
                        MAIN_ARTICLE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNewsAdapter.swapCursor(data);

        if(mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mBinding.newsRecyclerView.smoothScrollToPosition(mPosition);

        if(data.getCount() != 0) showResult();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

    @Override
    public void onClick(long _id) {
        Intent startDetailActivityIntent = new Intent(this, DetailActivity.class);
        Uri uri = NewsContract.NewsEntry.buildUriWithId(_id);
        startDetailActivityIntent.setData(uri);
        startActivity(startDetailActivityIntent);
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

    private void showLoading() {
        mBinding.loadingIndicator.setVisibility(View.VISIBLE);
        mBinding.newsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showResult() {
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.newsRecyclerView.setVisibility(View.VISIBLE);
    }
}
