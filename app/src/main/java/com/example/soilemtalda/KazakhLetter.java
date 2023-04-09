package com.example.soilemtalda;

import java.util.*;

class KazakhLetter {
    private String letter;
    private String description;
    private boolean isVowel;

    public KazakhLetter(String letter, String description, boolean isVowel) {
        this.letter = letter;
        this.description = description;
        this.isVowel = isVowel;
    }

    public String getLetter() {
        return letter;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVowel() {
        return isVowel;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", letter, description);
    }
}