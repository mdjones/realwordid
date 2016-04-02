package com.nibr.oncology.util.realwordid;

import com.google.common.collect.Sets;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;


/**
 * Check system for funcationality and that every ID is unique
 * Created by mjones on 4/2/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/application-context.xml"})
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
        boolean deleteWord = false;
        String aWord = uniqueWordFetcher.getUniqueEightLetterWord(deleteWord);
        assertNotNull("Word was null", aWord);

        Set<String> words = Sets.newHashSet();
        words.add(aWord);
        for(int i=0; i<100; i++){
            aWord = uniqueWordFetcher.getUniqueEightLetterWord(deleteWord);
            assertTrue("Not a unique word", !words.contains(aWord));
            words.add(aWord);
        }
    }

    @Test
    public void createDatabase() throws SQLException, IOException {
        dictionaryDao.createDataBase();
    }

    @Test
    public void testDao(){
        logger.debug(dictionaryDao.getRandomWord(8).getWord());
        logger.debug(dictionaryDao.getRandomWord(8).getWord());
        logger.debug(dictionaryDao.getRandomWord(8).getWord());
        logger.debug(dictionaryDao.getRandomWord(8).getWord());
    }
}
