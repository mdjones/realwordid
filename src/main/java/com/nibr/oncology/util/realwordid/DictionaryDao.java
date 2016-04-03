package com.nibr.oncology.util.realwordid;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Maps;
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

    void rebuildUsedWordsTable(){
        String sql = "DROP TABLE PUBLIC.USED_WORDS IF EXISTS";
        this.jdbcTemplate.execute(sql);
        logger.debug("Dropped PUBLIC.USED_WORDS");

        sql = "CREATE TABLE PUBLIC.USED_WORDS" +
                " (ID INTEGER NOT NUll, " +
                "SIZE INTEGER NOT NULL, " +
                "WORD VARCHAR(25) )";
        this.jdbcTemplate.execute(sql);
        logger.debug("CREATED USED_WORDS table");
    }

    void rebuildWordsTable() throws SQLException, IOException {
        int varcharSize = 25;
        TreeMap<Integer, Integer> wordSizeMap = Maps.newTreeMap();

        String sql = "DROP TABLE PUBLIC.WORDS IF EXISTS";
        this.jdbcTemplate.execute(sql);
        logger.debug("Dropped PUBLIC.WORDS");

        sql = "CREATE TABLE PUBLIC.WORDS" +
                " (ID INTEGER NOT NUll, " +
                "SIZE INTEGER NOT NULL, " +
                "WORD VARCHAR(" + varcharSize + ") )";
        this.jdbcTemplate.execute(sql);
        logger.debug("CREATED table");

        Resource resource = resourceLoader.getResource("classpath:allWords.txt");
        int n=0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String baseSql = "INSERT INTO PUBLIC.WORDS (ID, SIZE, WORD) VALUES (";
            String line;
            int batchSize = 100;
            int currentBatchSize = 0;
            int batchNumber = 0;
            sql = baseSql;
            while ((line = br.readLine()) != null) {
                String word = line.trim();
                n++;
                currentBatchSize++;
                //if(n>100000)break;
                sql += "(" + n + "," + "" + word.length() + ",'" + word + "'),";

                int count = 1;
                if(wordSizeMap.containsKey(word.length())){
                    count = wordSizeMap.get(word.length())+1;
                }
                wordSizeMap.put(word.length(), count);

                if(currentBatchSize == batchSize){
                    batchNumber++;
                    sql = sql.substring(0,sql.length()-1);
                    sql+=")";
                    logger.info("Committing batch " + batchNumber);
                    currentBatchSize = 0;
                    this.jdbcTemplate.execute(sql);
                    sql = baseSql;
                }
            }
        }

        if(wordSizeMap.lastKey()>varcharSize){
            logger.warn("Largest dictionary word (size=" + wordSizeMap.lastKey() +
                    " was greater then VARCHAR (size=" + varcharSize + ")");
        }
        logger.debug("Filled Table (Num. of Words = " + n + "). Largest word was " + wordSizeMap.lastKey());

        PrintWriter out
                = new PrintWriter(new BufferedWriter(new FileWriter("WORD_COUNT_TABLE.txt")));

        out.println("WORD_SIZE\tCOUNT");
        for(Map.Entry<Integer, Integer> entry : wordSizeMap.entrySet()){
            out.println(entry.getKey()+"\t"+entry.getValue());
        }
        out.flush();
        out.close();
    }

    public Word getRandomWord(final int size){
        String sql = "SELECT * FROM PUBLIC.WORDS WHERE SIZE = " + size +
                " ORDER BY RAND() LIMIT 1";
        return this.jdbcTemplate.query(sql, new ResultSetExtractor<Word>(){
            @Override
            public Word extractData(ResultSet rs) throws SQLException, DataAccessException {
                if(!rs.next()){
                    String msg = "No words of size " + size + " left in the database";
                    logger.error(msg);
                    throw new IllegalArgumentException(msg);
                }
                return new Word(rs.getInt("ID"), rs.getInt("SIZE"), rs.getString("WORD"));
            }
        });
    }

    void removeWord(Word word) {
        String sql = "DELETE FROM PUBLIC.WORDS " +
                "WHERE ID = " + word.getId();
        this.jdbcTemplate.execute(sql);
        sql = "INSERT INTO PUBLIC.USED_WORDS VALUES (" + word.getId() + "," + "" + word.getSize() + ",'" + word.getWord() + "')";
        this.jdbcTemplate.execute(sql);
    }
}
