package com.raffelberg.cr_ticker.activities;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.raffelberg.cr_ticker.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreference switchPreference_allMessages = findPreference("showMessages");
            SwitchPreference switchPreference_keyMessages = findPreference("showKeyMessages");

            //turn on notifications for keymessages when option for receiving every message is set to true
            assert switchPreference_allMessages != null;
            switchPreference_allMessages.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(newValue.equals(true)) {
                        assert switchPreference_keyMessages != null;
                        switchPreference_keyMessages.setChecked(true);
                    }
                    return true;
                }
            });
        }
    }
}