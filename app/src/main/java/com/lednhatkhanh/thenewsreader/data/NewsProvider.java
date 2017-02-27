package com.lednhatkhanh.thenewsreader.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lednhatkhanh.thenewsreader.utils.DataUtils;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsProvider extends ContentProvider {

    public static final int CODE_NEWS = 100;
    public static final int CODE_NEWS_WITH_TITLE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private NewsDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, NewsContract.PATH_NEWS, CODE_NEWS);
        uriMatcher.addURI(authority, NewsContract.PATH_NEWS + "?title=*", CODE_NEWS_WITH_TITLE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new NewsDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_NEWS:
                cursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_NEWS_WITH_TITLE:
                String title = uri.getQueryParameter("title");
                cursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        NewsContract.NewsEntry.COLUMN_TITLE + " = ? ",
                        new String[]{title},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_NEWS:
                db.beginTransaction();
                int rowsInserted = 0;

                try {
                    for(ContentValues value: values) {
                        long _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, value);
                        if(_id != -1) ++rowsInserted;
                    }
                } finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
