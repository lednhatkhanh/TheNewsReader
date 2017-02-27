package com.lednhatkhanh.thenewsreader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.lednhatkhanh.thenewsreader.data.NewsContract.NewsEntry;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE "
                + NewsEntry.TABLE_NAME + " ("
                + NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NewsEntry.COLUMN_TITLE + " TEXT NOT NULL UNIQUE, "
                + NewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + NewsEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + NewsEntry.COLUMN_URL + " TEXT NOT NULL, "
                + NewsEntry.COLUMN_URL_TO_IMAGE + " TEXT NOT NULL, "
                + NewsEntry.COLUMN_PUBLISHED_AT + " INTEGER NOT NULL"
                + ");";

        db.execSQL(SQL_CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME + ";");
        onCreate(db);
    }
}
