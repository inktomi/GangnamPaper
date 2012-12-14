package com.ignition.apps.zangnam.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class WallpaperPreferences {

    public static final String SHARED_PREFS = "wallpaper_shared_prefs";
    public static final String COLOR_FILTER = "door_color_filter";
    public static final String ELEVATOR_MUSIC = "elevator_music";

    public static int getDoorColorFilter(Context context) {
        return getSharedPreferences(context).getInt(COLOR_FILTER, 0);
    }

    public static void removeDoorColorFilter(Context context) {
        getSharedPreferences(context).edit().remove(COLOR_FILTER).commit();
    }

    public static boolean playElevatorMusic(Context context) {
        return getSharedPreferences(context).getBoolean(ELEVATOR_MUSIC, true);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }
}
