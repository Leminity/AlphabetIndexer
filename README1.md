# AlphabetIndexer
Android AhphabetIndexer.

* Usage
'''java'''
IndexerBar mIndexerBar = (IndexerBar) findViewById(R.id.indexerBar);
mIndexerBar.setOnIndexBarListener(new IndexerBar.OnIndexBarListener() {
    @Override
    public void onTouchingConsonantChanged(String consonant) {
        //Todo Select position by consonant
    }
});

//Return Consonant array(depends Settings > language)
String[] consonantArray = mIndexerBar.getConsonants();
for (String consonant : consonantArray)
    Log.d("leminity", "check to consonant :: " + consonant);

//Return Consonant unicode array(depends Settings > language)
List<ConsonantUnicode> consonantUnicodeList = mIndexerBar.getConsonantUnicodeList();
Log.d("leminity", "check to consonant Unicode List :: " + consonantUnicodeList);
'''java'''
