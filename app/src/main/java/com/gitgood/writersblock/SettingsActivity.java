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
    private static final String BATTERY_WARNING_PREFERENCE = "battery_warning_enabled";

    private Switch timerSwitch;
    private Switch batterySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        timerSwitch = findViewById(R.id.timer_switch);
        batterySwitch = findViewById(R.id.battery_switch);

        initializeSettings();

        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(SHARED_PREFERENCES,0).edit();

                sharedPreferencesEditor.putBoolean(TIMER_PREFERENCE, isChecked);
                sharedPreferencesEditor.apply();
            }
        });

        batterySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(SHARED_PREFERENCES,0).edit();

                sharedPreferencesEditor.putBoolean(BATTERY_WARNING_PREFERENCE, isChecked);
                sharedPreferencesEditor.apply();
            }
        });
    }

    private void initializeSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        Boolean timerSwitchEnabled = sharedPreferences.getBoolean(TIMER_PREFERENCE, true);
        Boolean batterySwitchEnabled = sharedPreferences.getBoolean(BATTERY_WARNING_PREFERENCE, true);

        timerSwitch.setChecked(timerSwitchEnabled);
        batterySwitch.setChecked(batterySwitchEnabled);
    }
}
