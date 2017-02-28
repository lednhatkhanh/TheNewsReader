package com.lednhatkhanh.thenewsreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.lednhatkhanh.thenewsreader.R;

/**
 * Created by lednh on 2/27/2017.
 */

public class PreferencesUtils {

    public static String getSource(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String sourcePrefKey = context.getString(R.string.pref_source_key);
        String sourcePrefDefault = context.getString(R.string.pref_source_default);
        return sharedPreferences.getString(sourcePrefKey, sourcePrefDefault);
    }

    public static String getSortBy(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String sortByPrefKey = context.getString(R.string.pref_sortBy_key);
        String sortByPrefDefault = context.getString(R.string.pref_sortBy_latest);
        return sharedPreferences.getString(sortByPrefKey, sortByPrefDefault);
    }
}
