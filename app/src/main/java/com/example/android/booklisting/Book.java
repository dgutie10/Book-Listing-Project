package com.example.android.booklisting;

import android.graphics.drawable.Drawable;

/**
 * Created by diegog on 11/3/2016.
 */

public class Book {
    private String title;
    private String author;
    private Drawable thumbnail;

    public Book(String title, String author, Drawable thumbnail) {
        this.title = title;
        this.author = author;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {return title;}

    public String getAuthor() {return author;}

    public Drawable getThumbnail() {return thumbnail;}
}
