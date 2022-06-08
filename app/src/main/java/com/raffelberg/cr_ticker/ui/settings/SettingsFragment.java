package com.raffelberg.cr_ticker.ui.settings;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.raffelberg.cr_ticker.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        setChangeListenerHerren();
        setChangeListenerDamen();
    }

    private void setChangeListenerHerren(){
        SwitchPreferenceCompat switchPreference_allNotifications = findPreference("all_notifications_herren1");
        SwitchPreferenceCompat switchPreference_importantNotifications = findPreference("important_notifications_herren1");

        //turn on notifications for keymessages when option for receiving every message is set to true
        assert switchPreference_allNotifications != null;
        switchPreference_allNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.equals(true)) {
                    assert switchPreference_importantNotifications != null;
                    switchPreference_importantNotifications.setChecked(true);
                    //TODO: subscribe
                }else{
                    //TODO: unsubscribe
                }
                return true;
            }
        });

        switchPreference_importantNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.equals(true)) {
                    //TODO: subscribe
                }else{
                    //TODO: unsubscribe
                }
                return true;
            }
        });
    }

    private void setChangeListenerDamen(){
        SwitchPreferenceCompat switchPreference_allNotifications = findPreference("all_notifications_damen1");
        SwitchPreferenceCompat switchPreference_importantNotifications = findPreference("important_notifications_damen1");

        //turn on notifications for keymessages when option for receiving every message is set to true
        assert switchPreference_allNotifications != null;
        switchPreference_allNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.equals(true)) {
                    assert switchPreference_importantNotifications != null;
                    switchPreference_importantNotifications.setChecked(true);
                    //TODO: subscribe
                }else{
                    //TODO: unsubscribe
                }
                return true;
            }
        });

        switchPreference_importantNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.equals(true)) {
                    //TODO: subscribe
                }else{
                    //TODO: unsubscribe
                }
                return true;
            }
        });
    }
}