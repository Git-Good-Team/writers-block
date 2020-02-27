package com.gitgood.writersblock;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WriterActivity extends AppCompatActivity {
    static final String TAG = WriterActivity.class.getSimpleName();
    static final String BASE_URL = "https://api.wordnik.com/v4/";
    static Retrofit retrofit = null;
    final static String API_KEY = "bdvw0bfb5fnxqa410rpswoi9qhe7fp5zg772hyseuv5wyny9e";

    public static boolean running;
    private boolean timerEnabled;
    private String viewTime ="";
    private TimerService time;
    private TextView timeView, verbView, nounView;
    private EditText write;
    private Button startButton;
    final Handler handler = new Handler();
    private BatteryReceiver batteryReceiver;
    private boolean bound = false;
    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String TIMER_PREFERENCE = "timer_enabled";


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
        verbView = (TextView) findViewById(R.id.tAdjective);
        nounView = (TextView) findViewById(R.id.tNoun);

        runTimer();
        registerBatteryReceiver();

        timeView = (TextView) findViewById(R.id.timer);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        timerEnabled = sharedPreferences.getBoolean(TIMER_PREFERENCE, true);

        if (timerEnabled) {
            runTimer();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (timerEnabled) {
            Intent intent = new Intent(this, TimerService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (timerEnabled) {
            if (bound) {
                unbindService(connection);
                bound = false;
            }
            handler.removeCallbacksAndMessages(null);
        }

        unregisterReceiver(batteryReceiver);
    }


    public void onClickStart(View view) {

        startButton = (Button) findViewById(R.id.startTimerButton);
        Button finishButton = findViewById(R.id.finishButton);

        startButton.setVisibility(View.INVISIBLE);
        finishButton.setVisibility(View.VISIBLE);
        timeView.setVisibility(View.VISIBLE);

        connectAdjective();
        connectNoun();

        running = true;
    }

    public void onClickFinish(View view) {
        Intent intent = new Intent(this, DraftDetailsActivity.class);
        startActivity(intent);
    }

    private void runTimer() {
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

    private void registerBatteryReceiver() {
        batteryReceiver = new BatteryReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, intentFilter);
    }

    //TODO: save contents of file and send to drafts
    public void timeOut() {
        DraftDatabaseHelper db = new DraftDatabaseHelper(getApplicationContext());

        EditText draft = findViewById(R.id.writerDraft);
        String stringDraft = draft.getText().toString();
        handler.removeCallbacksAndMessages(null);
        time.reset();
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

    private void connectAdjective() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        GenerateWordService wordAPI = retrofit.create(GenerateWordService.class);
        Call<Word> call = wordAPI.getVerb(API_KEY);
        call.enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {

                String value = response.body().getWord().toLowerCase();
                String verb = value.substring(0, 1).toUpperCase() + value.substring(1);
                verbView.setText(verb);

            }

            @Override
            public void onFailure(Call<Word> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }

    private void connectNoun() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        GenerateWordService wordAPI = retrofit.create(GenerateWordService.class);
        Call<Word> call = wordAPI.getNoun(API_KEY);
        call.enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {


                String value = response.body().getWord().toLowerCase();
                String noun = value.substring(0, 1).toUpperCase() + value.substring(1);
                nounView.setText(noun);
            }

            @Override
            public void onFailure(Call<Word> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }
}
