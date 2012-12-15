package com.zappos.android.zangnam.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import com.zappos.android.zangnam.R;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class WallpaperPreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ColorPickerPreference mColorFilter;
    private Preference mColorFilterReset;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(WallpaperPreferences.SHARED_PREFS);
        addPreferencesFromResource(R.xml.wallpaper_preferences);
        mColorFilter = (ColorPickerPreference) findPreference(getString(R.string.color_filter_key));
        mColorFilter.setAlphaSliderEnabled(false);
        mColorFilterReset = findPreference(getString(R.string.color_filter_reset_key));
        mColorFilterReset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                WallpaperPreferences.removeDoorColorFilter(WallpaperPreferencesActivity.this);
                restartActivity();
                return true;
            }
        });
        updateColorFilterResetState();
    }

    private void updateColorFilterResetState() {
        mColorFilterReset.setEnabled(WallpaperPreferences.getDoorColorFilter(this) != 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WallpaperPreferences.getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WallpaperPreferences.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TextUtils.equals(WallpaperPreferences.COLOR_FILTER, key)) {
            updateColorFilterResetState();
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
