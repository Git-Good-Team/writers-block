package com.gitgood.writersblock;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

                dailyWordTextView.setText(response.body().getWord());
                HashMap<String, List<String>> sortedDefinitions = sortDefinitions(response.body().getDefinitions());
                addDefinitionsToActivity(sortedDefinitions);
            }

            @Override
            public void onFailure(Call<DailyWord> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                Toast.makeText(getBaseContext(), R.string.api_connection_error_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private HashMap<String, List<String>> sortDefinitions(List<DailyWordDefinition> definitions) {
        HashMap<String, List<String>> definitionMap = new HashMap<>();

        for (DailyWordDefinition definition : definitions) {
            List<String> definitionSet = definitionMap.getOrDefault(definition.getPartOfSpeech(), new ArrayList<String>());
            definitionSet.add(definition.getDefinition());
            definitionMap.put(definition.getPartOfSpeech(), definitionSet);
        }

        return definitionMap;
    }

    private void addDefinitionsToActivity(HashMap<String, List<String>> definitionsMap) {
        Iterator iterator = definitionsMap.entrySet().iterator();
        LinearLayout layout = findViewById(R.id.daily_word_layout);

        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            addPartOfSpeechTextView(layout, (String) pair.getKey());

            int position = 1;
            for (String definition : (List<String>) pair.getValue()) {
                addDefinitionTextView(layout, definition, position);
                position++;
            }
        }
    }

    private void addPartOfSpeechTextView(LinearLayout layout, String partOfSpeech) {
        TextView partOfSpeechTextView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(10, 0, 0, 10);

        partOfSpeechTextView.setLayoutParams(layoutParams);
        partOfSpeechTextView.setText(partOfSpeech);
        partOfSpeechTextView.setTextColor(getResources().getColor(R.color.color_standard_text));
        partOfSpeechTextView.setTypeface(null, Typeface.ITALIC);

        layout.addView(partOfSpeechTextView);
    }

    private void addDefinitionTextView(LinearLayout layout, String definition, int position) {
        TextView definitionTextView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(10, 0, 0, 10);
        definitionTextView.setLayoutParams(layoutParams);
        definitionTextView.setTextColor(getResources().getColor(R.color.color_standard_text));
        definitionTextView.setText(position + ". " + definition);

        layout.addView(definitionTextView);
    }
}
