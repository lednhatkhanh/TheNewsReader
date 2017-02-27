package com.lednhatkhanh.thenewsreader.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsContract {

    public static final String CONTENT_AUTHORITY = "com.lednhatkhanh.thenewsreader";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NEWS = "news";

    public static final class NewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_NEWS)
                .build();

        public static final String TABLE_NAME = "news";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL= "url";
        public static final String COLUMN_URL_TO_IMAGE = "urlToImage";
        public static final String COLUMN_PUBLISHED_AT = "publishedAt";

        public static Uri buildUriWithTitle(String title) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter("title", title)
                    .build();
        }
    }
}
