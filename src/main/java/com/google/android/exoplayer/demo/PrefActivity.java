package com.google.android.exoplayer.demo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.exoplayer.demo.service.Storage;

/**
 * Created by raj on 6/11/16.
 */
public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preference, false);
    }


    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("bitRatePref")) {
                ListPreference bitRatePref = (ListPreference) getPreferenceManager().findPreference("bitRatePref");
                //bitRatePref.setSummary(sharedPreferences.getString("bitRatePref", ""));

                Storage.getInstance().setCurrentBitRate(sharedPreferences.getString("bitRatePref", ""));
            }

        }
    }
}
