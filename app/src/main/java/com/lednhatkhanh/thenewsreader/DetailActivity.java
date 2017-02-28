package com.lednhatkhanh.thenewsreader;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lednhatkhanh.thenewsreader.data.NewsContract;
import com.lednhatkhanh.thenewsreader.databinding.ActivityDetailBinding;
import com.lednhatkhanh.thenewsreader.utils.DataUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    ActivityDetailBinding mBinding;

    public static final String[] DETAIL_ARTICLE_PROJECTION = {
            NewsContract.NewsEntry._ID,
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsContract.NewsEntry.COLUMN_AUTHOR,
            NewsContract.NewsEntry.COLUMN_DESCRIPTION,
            NewsContract.NewsEntry.COLUMN_URL,
            NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE,
            NewsContract.NewsEntry.COLUMN_PUBLISHED_AT
    };
    public static final int INDEX_ARTICLE_ID = 0;
    public static final int INDEX_ARTICLE_TITLE = 1;
    public static final int INDEX_ARTICLE_AUTHOR = 2;
    public static final int INDEX_ARTICLE_DESCRIPTION = 3;
    public static final int INDEX_ARTICLE_URL = 4;
    public static final int INDEX_ARTICLE_URL_TO_IMAGE = 5;
    public static final int INDEX_ARTICLE_PUBLISHED_AT = 6;

    private static final int ID_DETAIL_LOADER = 353;
    private Uri mUri;
    private String urlToFullArticle;
    private String mShareString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        mUri = intent.getData();
        if (mUri == null) throw new NullPointerException("Null uri!");
        Toast.makeText(this, mUri.toString(), Toast.LENGTH_LONG).show();

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch(loaderId) {
            case ID_DETAIL_LOADER:
                Log.i("TRUE", mUri.toString());
                return new CursorLoader(this, mUri, DETAIL_ARTICLE_PROJECTION, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()) {
            Log.i("DATA", data.getString(INDEX_ARTICLE_URL_TO_IMAGE));

            String title = data.getString(INDEX_ARTICLE_TITLE);
            String author = data.getString(INDEX_ARTICLE_AUTHOR);
            mShareString = title + " by " + author;
            String formattedTime = DataUtils.getReadableDateFormat(
                    data.getLong(INDEX_ARTICLE_PUBLISHED_AT));

            Picasso.with(this).load(data.getString(INDEX_ARTICLE_URL_TO_IMAGE))
                    .fit()
                    .centerCrop()
                    .into(mBinding.articleDetailImageView);
            mBinding.articleDetailTitleTextView.setText(title);
            mBinding.articleDetailAuthorTextView.setText(author);
            mBinding.articleDetailPublishedAtTextView.setText(formattedTime);
            mBinding.articleDetailDescriptionTextView.setText(data.getString(INDEX_ARTICLE_DESCRIPTION));
            urlToFullArticle = data.getString(INDEX_ARTICLE_URL);
        } else {
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == mBinding.articleDetailUrlTextView.getId()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlToFullArticle)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_share) {
            startActivity(createShareIntent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mShareString + " #TheNewsReader")
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }
}
