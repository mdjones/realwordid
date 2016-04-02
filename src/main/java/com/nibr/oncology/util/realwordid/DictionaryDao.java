package com.nibr.oncology.util.realwordid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 *
 * Created by mjones on 4/2/2016.
 */
@Component
public class DictionaryDao {
    private static final Logger logger = LogManager.getLogger(DictionaryDao.class);

    private final JdbcTemplate jdbcTemplate;

    private final ResourceLoader resourceLoader;

    @Autowired
    public DictionaryDao(DataSource dataSource, ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    void createDataBase() throws SQLException, IOException {
        String sql = "DROP TABLE PUBLIC.WORDS IF EXISTS";
       this.jdbcTemplate.execute(sql);
        logger.debug("Dropped table");

         sql = "CREATE TABLE PUBLIC.WORDS" +
                 " (ID INTEGER NOT NUll, " +
                 "SIZE INTEGER NOT NULL, " +
                 "WORD CHAR(25)) ";
        this.jdbcTemplate.execute(sql);
        logger.debug("CREATED table");

//        sql = "CREATE TABLE PUBLIC.USED_WORDS" +
//                " (SIZE INTEGER NOT NULL, " +
//                "WORD CHAR(25) NOT NULL) ";
//        this.jdbcTemplate.execute(sql);
//        logger.debug("CREATED used words table");

        Resource resource = resourceLoader.getResource("classpath:allWords.txt");
        int n=0;
        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
            sql = "INSERT INTO PUBLIC.WORDS (ID, SIZE, WORD) VALUES (";
            String line;
            while ((line = br.readLine()) != null) {
                String word = line.trim();
                n++;
                if(n>500)break;
                sql += "(" + n + "," + "" + word.length() + ",'" + word + "'),";
            }
            sql = sql.substring(0,sql.length()-1);
            sql+=")";

            this.jdbcTemplate.execute(sql);
        }
        logger.debug("Filled Table");
    }

    public Word getRandomWord(int size){
        String sql = "SELECT * FROM PUBLIC.WORDS WHERE SIZE = " + size +
                " ORDER BY RAND() LIMIT 1";
        return this.jdbcTemplate.query(sql, new ResultSetExtractor<Word>(){
            @Override
            public Word extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return new Word(rs.getInt("ID"), rs.getInt("SIZE"), rs.getString("WORD"));
            }
        });
    }

    void removeWord(Word word) {
        String sql = "DELETE FROM PUBLIC.WORDS " +
                     "WHERE ID = " + word.getId();
        this.jdbcTemplate.execute(sql);
        sql = "INSERT INTO PUBLIC.USED_WORDS (" + word.getId() + "," + "" + word.getSize() + ",'" + word.getWord() + "')";
        this.jdbcTemplate.execute(sql);
    }
}
