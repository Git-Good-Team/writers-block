package com.gitgood.writersblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver {

    private static final double LOW_BATTERY_PERCENTAGE = 0.15;

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        double percentage = level * 1.0 / scale;

        System.out.println("Level: " + level);
        System.out.println("Scale: " + scale);
        System.out.println("Percentage :" + percentage);
        if (percentage <= LOW_BATTERY_PERCENTAGE) {
            Toast.makeText(context, R.string.low_battery_warning, Toast.LENGTH_LONG).show();
        }
    }
}
