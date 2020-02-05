package com.gitgood.writersblock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class WriterActivity extends AppCompatActivity {

    public static boolean running;
    private boolean flagDraft = true;
    private String viewTime ="";
    private TimerService time;
    private TextView timeView;
    private EditText write;

    private boolean bound = false;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            TimerService.TimeBinder timeBinder = (TimerService.TimeBinder) binder;
            time = timeBinder.getTimeService();
            bound = true;
            viewTime = time.getTime();
            timeView.setText(viewTime);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer);
        runTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, TimerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bound) {
            unbindService(connection);
            bound = false;
        }
    }


    public void onClickStart(View view) {
        running = true;
    }

    private void runTimer() {
        timeView = (TextView) findViewById(R.id.timer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(running) {
                    viewTime = time.getTime();
                    timeView.setText(viewTime);

                    // if time is zero then call timeOut to save draft
                    if(viewTime.equals("00:00")) {
                        timeOut();
                    }
                }

                handler.postDelayed(this,1000);
            }
        });
    }



    //TODO: save contents of file and send to drafts
    public void timeOut() {
        EditText draft = (EditText) findViewById(R.id.writerDraft);
        String stringDraft = draft.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("draft",stringDraft);
        startActivity(intent);
    }


}