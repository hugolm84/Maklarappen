/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import com.itm.projekt.Fragments.SettingsFragment;
import com.itm.projekt.MainActivity;
import com.itm.projekt.R;


public class SettingsPreferenceActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.title_settings));

        // Display the fragment as the main content.
        if (savedInstanceState == null)
            getFragmentManager().beginTransaction().add(android.R.id.content, new SettingsFragment()).commit();

        updateFragment();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

    }

    // Updates the fragment/view
    protected void updateFragment() {
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateFragment();
    }

    public String getPreferenceString(Integer id, Integer defaultId) {
        return preferences.getString(getResources().getString(id), getResources().getString(defaultId));
    }
}