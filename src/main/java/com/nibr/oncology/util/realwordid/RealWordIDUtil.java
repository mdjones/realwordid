package com.nibr.oncology.util.realwordid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

/**
 * CMD line app to work with the database
 * Created by mjones on 4/2/2016.
 */

@Component
public final class RealWordIdUtil {
    private static final Logger logger = LogManager.getLogger(RealWordIdUtil.class);

    public static final String URL_PARAM_KEY = "url";

    public static void main(String[] args) {

        // should use parameterless constructor as the other one invoke refresh which we certainly don't want
        // as it automatically trigger property injection and our CLI property is not ready yet
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        // setup configuration
        applicationContext.register(Config.class);

        // add CLI property source
        applicationContext.getEnvironment().getPropertySources().addLast(new SimpleCommandLinePropertySource(args));

        // setup all the dependencies (refresh) and make them run (start)
        applicationContext.refresh();
        applicationContext.start();

        try {
            String dbPath = applicationContext.getBean("databasePath", String.class);
            if(dbPath == null){
                usage("Please set " + Config.REAL_WORD_DB_PROP);

            }else {
                logger.debug("SDFSF" + applicationContext.getBean("databasePath"));

                RealWordIdUtil realWordIdUtil = applicationContext.getBean(RealWordIdUtil.class);
                System.out.println("realWordIdUtil: " + realWordIdUtil);
                UniqueWordFetcher uniqueWordFetcher
                        = applicationContext.getBean("uniqueWordFetcher", UniqueWordFetcher.class);
                System.out.println("uniqueWordFetcher: " + uniqueWordFetcher.getUniqueWord(9, false));
            }



        } finally {
            applicationContext.close();
        }

    }

    private static void usage(String s) {
        System.err.println(s);
    }
}
