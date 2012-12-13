package com.ignition.apps.gangnam;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class WallpaperPreferencesActivity extends PreferenceActivity {

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(WallpaperPreferences.SHARED_PREFS);
        addPreferencesFromResource(R.xml.wallpaper_preferences);
    }
}
