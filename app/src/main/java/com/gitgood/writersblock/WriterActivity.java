package com.gitgood.writersblock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
    private boolean flagDraft = true;
    private String viewTime ="";
    private TimerService time;
    private TextView timeView, verbView, nounView;
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
        verbView = (TextView) findViewById(R.id.tAdjective);
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

        startButton = (Button) findViewById(R.id.startTimerButton);
        startButton.setVisibility(View.INVISIBLE);
        timeView.setVisibility(View.VISIBLE);

        connectAdjective();
        connectNoun();

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
        EditText draft = findViewById(R.id.writerDraft);
        String stringDraft = draft.getText().toString();
        Intent intent = new Intent(this, DraftDetailsActivity.class);
        intent.putExtra("draft",stringDraft);
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
