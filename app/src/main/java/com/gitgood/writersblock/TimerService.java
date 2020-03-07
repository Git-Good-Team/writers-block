package com.gitgood.writersblock;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Locale;

public class TimerService extends Service {
    public TimerService() {
    }

    private static int seconds = 30;
    private final int RESET_TIME = seconds;

    private  final IBinder binder = new TimeBinder();

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
        WriterActivity.running = false;
    }

    public class TimeBinder extends Binder {
        TimerService getTimeService() {
            return TimerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public String getTime() {
        int minutes = (seconds%3600)/60;
        int secs = seconds%60;
        String time = String.format(Locale.getDefault(),"%02d:%02d",minutes,secs);
        if(WriterActivity.running) {
            seconds--;
            if(seconds < 0) {
                WriterActivity.running = false;
                seconds = RESET_TIME;
                return time;
            }
            return time;
        }
        return time;
    }

    public void reset() {
        seconds = RESET_TIME;
    }
}
