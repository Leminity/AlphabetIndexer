# AlphabetIndexer
Android AhphabetIndexer.

* Usage
'''java
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
'''
