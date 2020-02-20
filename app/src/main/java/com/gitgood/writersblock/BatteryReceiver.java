package com.gitgood.writersblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);

        final int currentLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        final int max = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        final int percentage = (int) Math.round((currentLevel * 100.0) / max);

        Log.v(null, "LEVEL" + percentage);

        if (percentage <= 15) {
            Toast.makeText(context, "Battery low. Consider charging your phone", Toast.LENGTH_LONG).show();
        }
    }
}
