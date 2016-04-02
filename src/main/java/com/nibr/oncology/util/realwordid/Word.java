package com.nibr.oncology.util.realwordid;

/**
 * Contains a word from the db
 * Created by mjones on 4/2/2016.
 */
public class Word {
    private int id;
    private int size;
    private String word;

    public Word(int id, int size, String word) {
        this.id = id;
        this.size = size;
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getWord() {
        return word;
    }
}
