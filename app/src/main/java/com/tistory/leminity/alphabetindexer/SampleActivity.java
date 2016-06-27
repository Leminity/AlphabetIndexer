package com.tistory.leminity.alphabetindexer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SampleActivity extends AppCompatActivity {

    private IndexerBar mIndexerBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


        mIndexerBar = (IndexerBar) findViewById(R.id.indexerBar);

        mIndexerBar.setIndexList(getResources().getStringArray(R.array.ary_alphabet_idx));
    }
}
