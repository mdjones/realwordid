# realwordid

pg29765.txt was downloded from

http://www.gutenberg.org/ebooks/29765

via:  curl http://www.gutenberg.org/cache/epub/29765/pg29765.txt > pg29765.txt

All words were extracted via.
grep -P "^[A-Z]+$" pg29765.txt | uniq > allWords.txt

# Usage

```bash
$ ./build/install/realwordid/bin/realwordid --help
Apr 03, 2016 12:33:24 AM org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@184f6be2: startup date [Sun Apr 03 00:33:24 EDT 2016]; root of context hierarchy
Option                    Description
------                    -----------
--help                    Print the help.
--randomWord [Integer]    Get 1 or more random words (default: 3)
--rebuildUsedWordsTable   Rebuild used word table.
--rebuildWordsTable       Rebuild word table.
--removeWordFromDataBase  Remove the returned word from the database
--wordSize [Integer]      The size of the word to return (default: 5)
```

## Rebuild the database
```bash
$ ./build/install/realwordid/bin/realwordid --rebuildUsedWordsTable --rebuildWordsTable
Apr 03, 2016 12:36:43 AM org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@184f6be2: startup date [Sun Apr 03 00:36:43 EDT 2016]; root of context hierarchy
00:36:44.531 [main] DEBUG com.nibr.oncology.util.realwordid.DictionaryDao - Dropped PUBLIC.WORDS
00:36:44.532 [main] DEBUG com.nibr.oncology.util.realwordid.DictionaryDao - CREATED table
00:36:44.533 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 1
00:36:44.536 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 2
00:36:44.539 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 3
00:36:44.542 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 4
00:36:44.546 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 5
00:36:44.548 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 6
00:36:44.549 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 7
00:36:44.551 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 8
00:36:44.553 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 9
00:36:44.555 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 10
00:36:44.562 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 11
00:36:44.564 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 12
00:36:44.565 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 13
00:36:44.566 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 14
00:36:44.567 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 15
00:36:44.569 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 16
00:36:44.571 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 17
00:36:44.572 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 18
00:36:44.572 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 19
00:36:44.573 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 20
00:36:44.574 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 21
00:36:44.575 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 22
00:36:44.576 [main] INFO  com.nibr.oncology.util.realwordid.DictionaryDao - Committing batch 23
...
```

## Get random uniq words

```bash
$ ./build/install/realwordid/bin/realwordid --randomWord 5 --wordSize 18 --removeWordFromDataBase
Apr 03, 2016 12:32:26 AM org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@184f6be2: startup date [Sun Apr 03 00:32:26 EDT 2016]; root of context hierarchy
00:32:27.637 [main] INFO  com.nibr.oncology.util.realwordid.RealWordApp - The word is BRANCHIOGASTROPODA
00:32:27.664 [main] INFO  com.nibr.oncology.util.realwordid.RealWordApp - The word is INCONSEQUENTIALITY
00:32:27.686 [main] INFO  com.nibr.oncology.util.realwordid.RealWordApp - The word is PARALLELOGRAMMATIC
00:32:27.710 [main] INFO  com.nibr.oncology.util.realwordid.RealWordApp - The word is CHROMOLITHOGRAPHER
00:32:27.733 [main] INFO  com.nibr.oncology.util.realwordid.RealWordApp - The word is THERMOLUMINESCENCE
Apr 03, 2016 12:32:27 AM org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
INFO: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@184f6be2: startup date [Sun Apr 03 00:32:26 EDT 2016]; root of context hierarchy
```
