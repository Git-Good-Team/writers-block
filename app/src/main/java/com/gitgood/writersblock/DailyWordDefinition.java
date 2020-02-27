package com.gitgood.writersblock;

import com.google.gson.annotations.SerializedName;

public class DailyWordDefinition {

    @SerializedName("text")
    private String definition;

    @SerializedName("partOfSpeech")
    private String partOfSpeech;

    public DailyWordDefinition(String definition, String partOfSpeech) {
        this.definition = definition;
        this.partOfSpeech = partOfSpeech;
    }

    public String getDefinition() {
        return definition;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }
}
