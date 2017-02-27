package com.lednhatkhanh.thenewsreader.data;

import android.provider.BaseColumns;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsContract {
    public static final class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL= "url";
        public static final String COLUMN_URL_TO_IMAGE = "urlToImage";
        public static final String COLUMN_PUBLISHED_AT = "publishedAt";
    }
}
