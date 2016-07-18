package com.tistory.leminity.alphabetindexer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class SampleActivity extends AppCompatActivity {

    private IndexerBar mIndexerBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


        mIndexerBar = (IndexerBar) findViewById(R.id.indexerBar);
        mIndexerBar.setOnIndexBarListener(new IndexerBar.OnIndexBarListener() {
            @Override
            public void onTouchingConsonantChanged(String consonant) {
                Toast.makeText(getBaseContext(), consonant, Toast.LENGTH_SHORT).show();
            }
        });

        String[] consonantArray = mIndexerBar.getConsonants();
        for (String consonant : consonantArray)
            Log.d("leminity", "check to consonant :: " + consonant);

        List<ConsonantUnicode> consonantUnicodeList = mIndexerBar.getConsonantUnicodeList();
        Log.d("leminity", "check to consonant Unicode List :: " + consonantUnicodeList);

    }
}
