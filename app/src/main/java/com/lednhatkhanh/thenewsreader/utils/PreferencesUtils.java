package com.lednhatkhanh.thenewsreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

import com.lednhatkhanh.thenewsreader.R;

/**
 * Created by lednh on 2/27/2017.
 */

public class PreferencesUtils {

    /**
     * Get the source param from the settings
     * @param context The application context
     * @return The source param
     */
    public static String getSource(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String sourcePrefKey = context.getString(R.string.pref_source_key);
        String sourcePrefDefault = context.getString(R.string.pref_source_default);
        return sharedPreferences.getString(sourcePrefKey, sourcePrefDefault);
    }

    /**
     * Get the sortBy param from the settings
     * @param context The application context
     * @return The sortBy param
     */
    public static String getSortBy(@NonNull final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String sortByPrefKey = context.getString(R.string.pref_sortBy_key);
        String sortByPrefDefault = context.getString(R.string.pref_sortBy_latest);
        return sharedPreferences.getString(sortByPrefKey, sortByPrefDefault);
    }

    /**
     * Check if the notifications are enabled or not
     * @param context The application context
     * @return True if the notifications are currently enabled
     */
    public static boolean areNotificationsEnabled(@NonNull final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        String prefNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean shouldEnableNotificationsByDefault = context.getResources()
                .getBoolean(R.bool.show_notifications_by_default);

        return sharedPreferences.getBoolean(prefNotificationsKey,
                shouldEnableNotificationsByDefault);
    }

    /**
     * Save the last notification time
     * @param context The application context
     * @param timeOfNotification Time of last notification
     */
    public static void saveLastNotificationTime(@NonNull final Context context,
                                                long timeOfNotification) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        editor.putLong(lastNotificationKey, timeOfNotification);
        editor.apply();
    }

    /**
     * Get last notification time
     * @param context The application context
     * @return The last notification time
     */
    public static long getLastNotificationTime(@NonNull final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        return sharedPreferences.getLong(lastNotificationKey, 0);
    }

    /**
     * Get the time elapsed time since the last notification
     * @param context The application context
     * @return The amount of time since the last notification
     */
    public static long getElapsedTimeSinceLastNotification(@NonNull final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        return System.currentTimeMillis() - getLastNotificationTime(context);
    }
}
