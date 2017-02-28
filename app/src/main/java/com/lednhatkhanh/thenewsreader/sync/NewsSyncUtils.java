package com.lednhatkhanh.thenewsreader.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsSyncUtils {
    public static void startSynching(@NonNull final Context context) {
        Intent intent = new Intent(context, NewsSyncIntentService.class);
        context.startService(intent);
    }
}
