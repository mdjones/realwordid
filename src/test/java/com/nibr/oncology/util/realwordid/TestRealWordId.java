package com.nibr.oncology.util.realwordid;

import com.google.common.collect.Sets;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;


/**
 * Check system for funcationality and that every ID is unique
 * Created by mjones on 4/2/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Config.class, loader=AnnotationConfigContextLoader.class)
public class TestRealWordId {
    private static final Logger logger = LogManager.getLogger(TestRealWordId.class);

    @Autowired
    UniqueWordFetcher uniqueWordFetcher;

    @Autowired
    DictionaryDao dictionaryDao;

    @Test
    public void aTest() {
        System.out.println("Should see a log output");
        logger.info("Logging Test");
    }

    @Test
    public void getARandomWord(){
        assertNotNull("null uniqueWordFetcher", uniqueWordFetcher);
        boolean removeWordFromDb = true;
        String aWord = uniqueWordFetcher.getUniqueFiveLetterWord(removeWordFromDb);
        assertNotNull("Word was null", aWord);

        Set<String> words = Sets.newTreeSet();
        words.add(aWord);
        int wordsToFetch = 2000;
        for(int i=1; i<wordsToFetch; i++){
            aWord = uniqueWordFetcher.getUniqueFiveLetterWord(removeWordFromDb);
            assertTrue("Not a unique word, aWord="+aWord +", words="+words, !words.contains(aWord));
            words.add(aWord);
        }

        assertEquals("Did not get all the words", wordsToFetch, words.size());
    }

    @Test
    public void createDatabase() throws SQLException, IOException {
        dictionaryDao.rebuildUsedWordsTable();
        dictionaryDao.rebuildUsedWordsTable();
    }

    @Test
    public void testDao(){
        logger.debug("|"+dictionaryDao.getRandomWord(5).getWord()+"|");
        logger.debug("|"+dictionaryDao.getRandomWord(5).getWord()+"|");
        logger.debug("|"+dictionaryDao.getRandomWord(5).getWord()+"|");
        logger.debug("|"+dictionaryDao.getRandomWord(5).getWord()+"|");
    }
}
