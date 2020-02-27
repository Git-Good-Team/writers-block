package com.gitgood.writersblock;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyWord {

    @SerializedName("word")
    private String word;

    @SerializedName("definitions")
    private List<DailyWordDefinition> definitions;

    public DailyWord(String word, List<DailyWordDefinition> definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public List<DailyWordDefinition> getDefinitions() {
        return definitions;
    }
}
