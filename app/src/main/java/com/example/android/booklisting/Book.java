package com.example.android.booklisting;

/**
 * Created by diegog on 11/3/2016.
 */

public class Book {
    private String title;
    private String author;
    private int thumbnail;

    public Book(String title, String author, int thumbnail) {
        this.title = title;
        this.author = author;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {return title;}

    public String getAuthor() {return author;}

    public int getThumbnail() {return thumbnail;}
}
