package com.ignition.apps.gangnam;

import android.content.Context;
import android.content.SharedPreferences;

public class WallpaperPreferences {

    public static final String SHARED_PREFS = "wallpaper_shared_prefs";
    public static final String COLOR_FILTER = "door_color_filter";

    public static int getDoorColorFilter(Context context) {
        return getSharedPreferences(context).getInt(COLOR_FILTER, 0);
    }

    public static void removeDoorColorFilter(Context context) {
        getSharedPreferences(context).edit().remove(COLOR_FILTER).commit();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }
}
