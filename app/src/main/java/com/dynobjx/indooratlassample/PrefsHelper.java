package com.dynobjx.indooratlassample;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.Arrays;
import java.util.List;

public class PrefsHelper {

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0);
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static float getFloat(Context context, String key) {
        return getPrefs(context).getFloat(key, 0f);
    }



    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0L);
    }

    public static boolean getBoolean(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    public static void setBoolean(Context context, String key,
                                  boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    public static void setString(Context context, String key, String value) {
            getPrefs(context).edit().putString(key, value).commit();
    }

    public static void setInt(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    public static void setFloat(Context context, String key, Float value) {
        getPrefs(context).edit().putFloat(key, value).commit();
    }

    public static void setLong(Context context, String key, Long value) {
        getPrefs(context).edit().putLong(key, value).commit();
    }
}
