package com.gitgood.writersblock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class WriterActivity extends AppCompatActivity {

    public static boolean running;
    private boolean flagDraft = true;
    private String viewTime ="";
    private TimerService time;
    private TextView timeView, adjView, nounView;
    private EditText write;
    private Button startButton;
    final Handler handler = new Handler();

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
        adjView = (TextView) findViewById(R.id.tAdjective);
        nounView = (TextView) findViewById(R.id.tNoun);
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
        handler.removeCallbacksAndMessages(null);
    }


    public void onClickStart(View view) {
        adjView.setText("Angry");
        nounView.setText("Mob");

        startButton = (Button) findViewById(R.id.startTimerButton);
        startButton.setVisibility(View.INVISIBLE);
        timeView.setVisibility(View.VISIBLE);

        running = true;
    }

    private void runTimer() {
        timeView = (TextView) findViewById(R.id.timer);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(running) {
                    viewTime = time.getTime();
                    timeView.setText(viewTime);

                    // if time is zero then call timeOut to save draft
                    if(viewTime.equals("00:00")) {
                        timeView.setText("Finished!");
                        timeOut();
                    }
                }
                handler.postDelayed(this,1000);
            }
        });
    }



    //TODO: save contents of file and send to drafts
    public void timeOut() {
        DraftDatabaseHelper db = new DraftDatabaseHelper(getApplicationContext());

        EditText draft = findViewById(R.id.writerDraft);
        String stringDraft = draft.getText().toString();
        Intent intent = new Intent(this, DraftDetailsActivity.class);


        TextView adj = findViewById(R.id.tAdjective);
        TextView noun = findViewById(R.id.tNoun);

        String title = adj.getText().toString()+ " " + noun.getText().toString();

        long rowId = db.insertDraft(title, stringDraft);
        System.out.println(rowId);

        intent.putExtra("id", rowId);

        db.close();
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        if (!running){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
