/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.itm.projekt.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public String toString() {
        return "Inställningar";
    }
}