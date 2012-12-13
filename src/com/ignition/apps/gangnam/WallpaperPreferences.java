package com.ignition.apps.gangnam;

import android.content.Context;
import android.content.SharedPreferences;

public class WallpaperPreferences {

    public static final String SHARED_PREFS = "wallpaper_shared_prefs";
    public static final String COLOR_FILTER = "color_filter";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }
}
