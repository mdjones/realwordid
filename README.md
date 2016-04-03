# realwordid

pg29765.txt was downloded from

http://www.gutenberg.org/ebooks/29765

via:  curl http://www.gutenberg.org/cache/epub/29765/pg29765.txt > pg29765.txt

All words were extracted via.
grep -P "^[A-Z]+$" pg29765.txt | uniq > allWords.txt

# Usage

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

# Rebuild the database
$ ./build/install/realwordid/bin/realwordid --rebuildUsedWordsTable --rebuildWordsTable

#Get random uniq words

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

