package com.gitgood.writersblock;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenerateWordService {

    @GET("words.json/randomWord?hasDictionaryDef=true&includePartOfSpeech=verb-transitive&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=3&maxLength=10&")
    Call<Word> getVerb(@Query("api_key") String apiKey);

    @GET("words.json/randomWord?hasDictionaryDef=true&includePartOfSpeech=noun&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=4&maxLength=7&")
    Call<Word> getNoun(@Query("api_key") String apiKey);
}
