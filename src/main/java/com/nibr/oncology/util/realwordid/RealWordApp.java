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
public final class RealWordApp {
    private static final Logger logger = LogManager.getLogger(RealWordApp.class);

    private final UniqueWordFetcher uniqueWordFetcher;
    private final DictionaryDao dictionaryDao;

    @Autowired
    public RealWordApp(final UniqueWordFetcher uniqueWordFetcher, DictionaryDao dictionaryDao) {
        this.uniqueWordFetcher = uniqueWordFetcher;
        this.dictionaryDao = dictionaryDao;
    }

    public static void main(String[] args) throws IOException, SQLException {
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        try {
// add CLI property source
            OptionParser parser = new OptionParser();
            parser.accepts("help", "Print the help.");
            parser.accepts("rebuildWordsTable", "Rebuild word table.");
            parser.accepts("rebuildUsedWordsTable", "Rebuild used word table.");
            parser.accepts("removeWordFromDataBase", "Remove the returned word from the database");
            parser.accepts("randomWord", "Get 1 or more random words").withOptionalArg().ofType( Integer.class ).defaultsTo(3);
            parser.accepts("wordSize", "The size of the word to return").withOptionalArg().ofType( Integer.class ).defaultsTo(5);
            OptionSet options = parser.parse(args);

            if(!System.getenv().containsKey(Config.REAL_WORD_DB_PROP)){
                System.out.println();
                logger.error("Need to set " + Config.REAL_WORD_DB_PROP);
                logger.error("'$ export " + Config.REAL_WORD_DB_PROP + "=/opt/var/words/realworddb'");
                System.out.println();
                parser.printHelpOn(System.out);
                System.exit(0);
            }

            if(!options.hasOptions() || options.has("help")) {
                parser.printHelpOn(System.out);
                System.exit(0);
            }

            RealWordApp realWordIdUtil = makeRealWordIdUtil(applicationContext);

            if(options.has("rebuildWordsTable")){
                realWordIdUtil.rebuildWordsTable();
            }

            if(options.has("rebuildUsedWordsTable")){
                realWordIdUtil.rebuildUsedWordsTable();
            }

            if(options.has("randomWord")){
                int wordSize = Integer.parseInt(options.valueOf("wordSize").toString());
                int n = Integer.parseInt(options.valueOf("randomWord").toString());
                boolean removeFromDataBase = options.has("removeWordFromDataBase");
                for(int i=0; i<n; i++) {
                    realWordIdUtil.printRandomWord(wordSize, removeFromDataBase);
                }
            }


        } finally {
            applicationContext.close();
        }

    }

    private static RealWordApp makeRealWordIdUtil(AnnotationConfigApplicationContext applicationContext) {
        // should use parameterless constructor as the other one invoke refresh which we certainly don't want
        // as it automatically trigger property injection and our CLI property is not ready yet

        // setup configuration
        applicationContext.register(Config.class);

        // setup all the dependencies (refresh) and make them run (start)
        applicationContext.refresh();
        applicationContext.start();
        RealWordApp realWordApp = applicationContext.getBean(RealWordApp.class);
        return realWordApp;
    }

    private void rebuildWordsTable() throws IOException, SQLException {
        dictionaryDao.rebuildWordsTable();
    }

    private void rebuildUsedWordsTable() throws IOException, SQLException {
        dictionaryDao.rebuildUsedWordsTable();
    }


    private void printRandomWord(int wordSize, boolean removeFromDataBase) {
        System.out.println(uniqueWordFetcher.getUniqueWord(wordSize, removeFromDataBase));
    }
}
