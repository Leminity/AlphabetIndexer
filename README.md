# AlphabetIndexer
Android AhphabetIndexer.
(support ko, jp, en)

1.Sample Code
```java
mIndexerBar = (IndexerBar) findViewById(R.id.indexerBar);
mIndexerBar.setOnIndexBarListener(new IndexerBar.OnIndexBarListener() {
    @Override
    public void onTouchingConsonantChanged(String consonant) {
       //Todo select position in list by consonant.
    }
});

//Return consonant array(depends Device Settings > Language)
String[] consonantArray = mIndexerBar.getConsonants();
for (String consonant : consonantArray)
    Log.d("leminity", "check to consonant :: " + consonant);

//Return consonant unicode array(depends Device Settings > Language)
List<ConsonantUnicode> consonantUnicodeList = mIndexerBar.getConsonantUnicodeList();
Log.d("leminity", "check to consonant Unicode List :: " + consonantUnicodeList);
```
