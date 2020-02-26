package com.gitgood.writersblock;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Word {

    @SerializedName("word")
    private String word;


    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
