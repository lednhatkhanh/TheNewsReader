package com.lednhatkhanh.thenewsreader.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsSyncIntentService extends IntentService {
    public NewsSyncIntentService() {
        super("NewsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NewsSyncTask.syncNews(this);
    }
}
