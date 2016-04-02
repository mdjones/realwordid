package com.nibr.oncology.util.realwordid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Pulls random unique words from the database
 * Created by mjones on 4/2/2016.
 */
@Component
public class UniqueWordFetcher {

    private final DictionaryDao dictionaryDao;

    @Autowired
    public UniqueWordFetcher(DictionaryDao dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
    }

    public String getUniqueWord(int size, boolean removeWordFromDb){
        Word word = dictionaryDao.getRandomWord(size);
        if(removeWordFromDb) {
            dictionaryDao.removeWord(word);
        }
        return word.getWord();
    }

    /**
     * There are more 8 letter words in the dictionary then any other size word.
     * There are 13,552 such instances.
     *
     * @return unique 8 letter word.
     */
    public String getUniqueEightLetterWord(boolean removeWordFromDb){
        return getUniqueWord(8, removeWordFromDb);
    }

    public String getUniqueFiveLetterWord(boolean removeWordFromDb) {
        return getUniqueWord(5, removeWordFromDb);
    }
}
