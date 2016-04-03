package com.nibr.oncology.util.realwordid;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

/**
 * CMD line app to work with the database
 * Created by mjones on 4/2/2016.
 */

@Component
public final class RealWordIdUtil {
    private static final Logger logger = LogManager.getLogger(RealWordIdUtil.class);

    private final UniqueWordFetcher uniqueWordFetcher;
    private final DictionaryDao dictionaryDao;

    @Autowired
    public RealWordIdUtil(final UniqueWordFetcher uniqueWordFetcher, DictionaryDao dictionaryDao) {
        this.uniqueWordFetcher = uniqueWordFetcher;
        this.dictionaryDao = dictionaryDao;
    }

    public static void main(String[] args) throws IOException, SQLException {

        // should use parameterless constructor as the other one invoke refresh which we certainly don't want
        // as it automatically trigger property injection and our CLI property is not ready yet
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        // setup configuration
        applicationContext.register(Config.class);


        // setup all the dependencies (refresh) and make them run (start)
        applicationContext.refresh();
        applicationContext.start();

        try {
            RealWordIdUtil realWordIdUtil = applicationContext.getBean(RealWordIdUtil.class);
// add CLI property source
            OptionParser parser = new OptionParser();
            parser.accepts("rebuildDatabase", "Rebuild word table.");
            parser.accepts("removeUsedWordList", "Rebuild used word table.");
            parser.accepts("removeWordFromDataBase", "Remove the returned word from the database");
            parser.accepts("randomWord").withOptionalArg().ofType( Integer.class ).defaultsTo(5);
            OptionSet options = parser.parse(args);
            //PropertySource ps = new JOptCommandLinePropertySource(options);
            //applicationContext.getEnvironment().getPropertySources().addLast(ps);

            //logger.debug(applicationContext.getEnvironment());
            if(options.has("randomWord")){
                int wordSize = Integer.parseInt(options.valueOf("randomWord").toString());
                boolean removeFromDataBase = options.has("removeWordFromDataBase");
                realWordIdUtil.printRandomWord(wordSize, removeFromDataBase);
            }
            if(options.has("rebuildDatabase")){
                realWordIdUtil.rebuildDataBase();
            }

            if(!options.hasOptions() || options.has("help")) {
                parser.printHelpOn(System.out);
                System.exit(0);
            }

        } finally {
            applicationContext.close();
        }

    }

    private void rebuildDataBase() throws IOException, SQLException {
        dictionaryDao.rebuildUsedWordsTable();
        dictionaryDao.createDataBase();
    }

    private void printRandomWord(int wordSize, boolean removeFromDataBase) {
        logger.info("The word is " + uniqueWordFetcher.getUniqueWord(wordSize, removeFromDataBase));
    }
}
