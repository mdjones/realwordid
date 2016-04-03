package com.nibr.oncology.util.realwordid;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Config
 * Created by mjones on 4/2/2016.
 */
@Configuration
@ComponentScan("com.nibr") // search the com.company package for @Component classes
@ImportResource({"classpath:application-context.xml"})
public class Config {
    static String REAL_WORD_DB_PROP = "REAL_WORD_DB";
    private static final Logger logger = LogManager.getLogger(Config.class);

    @Value("${jdbc.driverClassName}")
    private String driverClassName;

    @Value("${jdbc.username}")
    private String userName;

    @Value("${jdbc.password}")
    private String password;

    @Autowired
    private Environment environment;

    @Bean
    public String databasePath() {
        return environment.getProperty(REAL_WORD_DB_PROP);
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        logger.debug("MASDASDASD:" + userName);
        dataSource.setDriverClassName(driverClassName);

        if (!environment.containsProperty(REAL_WORD_DB_PROP)) {
            throw new IllegalArgumentException("EEnvironment variable " + REAL_WORD_DB_PROP + " must be set");
        }
        String url = "jdbc:hsqldb:file:" + environment.getProperty(REAL_WORD_DB_PROP);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }


}
