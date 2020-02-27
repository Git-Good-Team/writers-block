package com.gitgood.writersblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String TIMER_PREFERENCE = "timer_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeSettings();
        Switch timerSwitch = findViewById(R.id.timer_switch);

        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(SHARED_PREFERENCES,0).edit();

                sharedPreferencesEditor.putBoolean(TIMER_PREFERENCE, isChecked);
                sharedPreferencesEditor.apply();
            }
        });
    }

    private void initializeSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        Switch timerSwitch = findViewById(R.id.timer_switch);
        Boolean timerSwitchEnabled = sharedPreferences.getBoolean(TIMER_PREFERENCE, true);

        timerSwitch.setChecked(timerSwitchEnabled);
    }
}
