package com.gitgood.writersblock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DailyWordActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.wordnik.com/v4/";
    private static final String TAG = DailyWordActivity.class.getSimpleName();
    private static Retrofit retrofit = null;
    private final static String API_KEY = "bdvw0bfb5fnxqa410rpswoi9qhe7fp5zg772hyseuv5wyny9e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_word_activity);

        getWordOfTheDay();
    }

    private void getWordOfTheDay() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        GenerateWordService wordAPI = retrofit.create(GenerateWordService.class);
        Call<DailyWord> dailyWordCall = wordAPI.getWordOfTheDay(API_KEY);

        dailyWordCall.enqueue(new Callback<DailyWord>() {
            @Override
            public void onResponse(Call<DailyWord> call, Response<DailyWord> response) {
                TextView dailyWordTextView = findViewById(R.id.daily_word_text_view);
                TextView definitionTextView = findViewById(R.id.daily_word_definition);

                dailyWordTextView.setText(response.body().getWord());
                definitionTextView.setText(response.body().getDefinitions().get(0).getDefinition());
            }

            @Override
            public void onFailure(Call<DailyWord> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                Toast.makeText(getBaseContext(), R.string.api_connection_error_message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
