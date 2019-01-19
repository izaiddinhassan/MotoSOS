package com.basikal.motosos;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SwitchPreferenceCompat mNightModeSwitch;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
        getActivity().setTitle("Settings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNightModeSwitch = (SwitchPreferenceCompat) getPreferenceManager().findPreference("night_mode");
        mNightModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast.makeText(getContext(), "123123123", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
