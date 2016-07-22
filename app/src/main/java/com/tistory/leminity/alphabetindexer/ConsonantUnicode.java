package com.tistory.leminity.alphabetindexer;

import java.util.Arrays;

/**
 * Created by User on 2016-07-18.
 */
public class ConsonantUnicode {

    private String consonant;
    private char[] unicodeArray; //required define unicode even number

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

    public boolean isContainUnicode(String args) {
        char consonantArgs = getConsonantChar(args);

        for (int i = 0; i < (unicodeArray.length / 2); i++) {
            if((unicodeArray[i] >= consonantArgs)
                    && (unicodeArray[i+1] <= consonantArgs)) {
                return true;
            }
        }

        return false;
    }

    private static char getConsonantChar(String args) {
        char consonant;
        char comVal = (char) (args.trim().charAt(0)-0xAC00);

        if (comVal >= 0 && comVal <= 11172){ // kr
            char uniVal = (char)comVal; // ignore if inputed only initial consonant.

            char cho = (char) ((((uniVal - (uniVal % 28)) / 28) / 21) + 0x1100); // divide initial consonant depends unicode
            consonant = cho;

        } else { // if not kr
            comVal = (char) (comVal+0xAC00);
            consonant = comVal;
        }

        return consonant;
    }

    @Override
    public String toString() {
        return "ConsonantUnicode{" +
                "consonant='" + consonant + '\'' +
                ", unicodeArray=" + Arrays.toString(unicodeArray) +
                '}';
    }
}
