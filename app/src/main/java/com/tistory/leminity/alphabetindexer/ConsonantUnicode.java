package com.tistory.leminity.alphabetindexer;

import java.util.Arrays;

/**
 * Created by User on 2016-07-18.
 */
public class ConsonantUnicode {

    private String consonant;
    private char[] unicodeArray;

    public ConsonantUnicode(String consonant, char[] unicodeArray) {
        this.consonant = consonant;
        this.unicodeArray = unicodeArray;
    }

    public String getConsonant() {
        return consonant;
    }

    public char[] getUnicodeArray() {
        return unicodeArray;
    }

    @Override
    public String toString() {
        return "ConsonantUnicode{" +
                "consonant='" + consonant + '\'' +
                ", unicodeArray=" + Arrays.toString(unicodeArray) +
                '}';
    }
}
