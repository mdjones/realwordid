package com.nibr.oncology.util.realwordid;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Config
 * Created by mjones on 4/2/2016.
 */
@Configuration
@ComponentScan("com.nibr") // search the com.company package for @Component classes
@PropertySource("classpath:/jdbc.properties")
public class Config {
    static String REAL_WORD_DB_PROP = "REAL_WORD_DB";
    private static final Logger logger = LogManager.getLogger(Config.class);

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));

        if (!environment.containsProperty(REAL_WORD_DB_PROP)) {
            throw new IllegalArgumentException("EEnvironment variable " + REAL_WORD_DB_PROP + " must be set");
        }
        String url = "jdbc:hsqldb:file:" + environment.getProperty(REAL_WORD_DB_PROP);
        dataSource.setUrl(url);
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }


}
